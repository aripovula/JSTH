package com.jsthf.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsthf.model.FCExItem;
import com.jsthf.model.User;
import com.jsthf.other.FlashMessage;
import com.jsthf.other.Framework;
import com.jsthf.other.LoadOptions;
import com.jsthf.other.OriginOptions;
import com.jsthf.other.SortOptions;
import com.jsthf.other.SortingEngine;
import com.jsthf.other.TagOptionsMatch;
import com.jsthf.other.TagOptionsRule;
import com.jsthf.other.Topic;
import com.jsthf.other.TypeOptions;
import com.jsthf.other.UserValidator;
import com.jsthf.service.RiskService;
import com.jsthf.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RiskService riskService;

	@Autowired
	private SortingEngine sortEngine;

	private static Logger myLogger = Logger
			.getLogger(UserController.class.getName());


	@GetMapping("/welcome")
	public String welcomeUser(Model model) {
		return "welcome";
	}

	@GetMapping("/resume")
	public String myCv(Model model) {
		return "resume";
	}

	@GetMapping("/appsummary")
	public String viewsource(Model model) {
		return "summary";
	}

	@GetMapping("/sourcecode")
	public String readsource(Model model) {

		String  allCodeInJsonFormat = sortEngine.getSourceCodeText();
		
		model.addAttribute("allCodeInJsonFormat", allCodeInJsonFormat);
		
		return "codeviewer";
	}
	
	// Form for selecting cards
	@GetMapping("/customload")
	public String formCustomLoad(Model model, Principal principal) {

		final String currentUser = principal.getName();

		User userF;
		// condition below gets boolean from Service thru Dao
		if (userService.existsByName(currentUser)) {
			long currentUserId = userService.idByName(currentUser);
			userF = userService.findById(currentUserId);
		} else {
			userF = new User();
		}
		
		if (!model.containsAttribute("user")) {
			userF.setUsername(currentUser);
			model.addAttribute("user", userF);
		}

		String topicsjsn = sortEngine.getTopicsInJSONformat();

		String risksjsn = sortEngine.getRisksInJSONformat(currentUser);

		model.addAttribute("frameworks", Framework.values());
		model.addAttribute("origins", OriginOptions.values());
		model.addAttribute("typeoptions", TypeOptions.values());
		model.addAttribute("loadoptions", LoadOptions.values());
		model.addAttribute("sortoptions", SortOptions.values());
		model.addAttribute("tags", sortEngine.getTags(currentUser));
		model.addAttribute("tagrule", TagOptionsRule.values());
		model.addAttribute("tagmatch", TagOptionsMatch.values());
		model.addAttribute("topics", Topic.values());
		model.addAttribute("topicsjsn", topicsjsn);
		model.addAttribute("risks", riskService.findAllRisksAddedByUserAndAdmin(currentUser));
		model.addAttribute("risksjsn", risksjsn);
		model.addAttribute("action", "/customload");
		model.addAttribute("heading", "Custom load cards");
		model.addAttribute("submit", "Next >");

		model.addAttribute("begincolumn", "<tr>");
		model.addAttribute("endcolumn", "</tr>");

		return "customselectform";
	}

	// sort cards based on option selected
	@PostMapping(value = "/customload")
	public String sortCards(Model model, @ModelAttribute("user") User user,
			BindingResult result, RedirectAttributes redirectAttr) {

		// since cards in new selection should start from 0 set counter int to 0
		user.setCurrentCardId(0);
		sortEngine.setIsNewSelection(true);

		// custom HasErrors class - from
		// https://stackoverflow.com/questions/12146298/spring-mvc-how-to-perform-validation
		UserValidator userValidator = new UserValidator();
		userValidator.validate(user, result);

		if (result.hasErrors()) {

			redirectAttr.addFlashAttribute("user", user);
			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please select card, order and load types  !", FlashMessage.Status.FAILURE));

			return "redirect:/customload";
		}
		
		// if keyword search is selected and no keyword typed
		if (user.getLoadTypeSeln().equals("1") && user.getKeywordSeln() == null
			|| user.getLoadTypeSeln().equals("1") && user.getKeywordSeln() != null && user.getKeywordSeln().length() == 0) {

			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please type a keyword !", FlashMessage.Status.FAILURE));
			redirectAttr.addFlashAttribute("user", user);

			return "redirect:/customload";
		}

		// check if incomplete selection was made for tags based search
		// and if not generate error message
		if (user.getLoadTypeSeln().equals("4")) {
			
			boolean tags = user.getTagsSeln() != null && user.getTagsSeln().size() > 0;
			boolean rule = user.getTagsRule() != null && user.getTagsRule().length() > 0;
			boolean match = user.getTagsMatch() != null && user.getTagsMatch().length() > 0;

			int tagsCount;
			String tg = user.getTagsSeln().toString();
			if (tg != null && tg.length() > 0) {
				List<String> tgs = Arrays.asList(tg.split(","));
				tagsCount = tgs.size();
			} else {
				tagsCount = 0;
			}

			boolean isAnyMissing = false;
			String message = "";
			String message2 = "";

			if (!tags) {
				isAnyMissing = true;
				message = "tick at least one tag";
			}
			if (!match && !rule && tagsCount > 1) {
				isAnyMissing = true;
				message2 = "match rule and sort rule";
			} else if (!match) {
				isAnyMissing = true;
				message2 = "match rule";
			} else if (!rule && tagsCount > 1) {
				isAnyMissing = true;
				message2 = "sort rule";
			}

			if (isAnyMissing) {
				if (message.length() > 0 && message2.length() > 0) {
					message = "Please " + message + " and select " + message2;
				} else if (message.length() > 0 && message2.length() == 0) {
					message = "Please " + message;
				} else if (message.length() == 0 && message2.length() > 0) {
					message = "Please select " + message2;
				}

				redirectAttr.addFlashAttribute("flash",
						new FlashMessage(message, FlashMessage.Status.FAILURE));
				redirectAttr.addFlashAttribute("user", user);
				return "redirect:/customload";
			}
		}

		if (user.getLoadTypeSeln().equals("5") && user.getTopicsSeln() == null
				|| user.getLoadTypeSeln().equals("5") && user.getTopicsSeln() != null && user.getTopicsSeln().size() == 0) {
			
			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please select at least one topic !", FlashMessage.Status.FAILURE));
			redirectAttr.addFlashAttribute("user", user);
			return "redirect:/customload";
		}

		if (user.getLoadTypeSeln().equals("6") && user.getRisksSeln() == null
				|| user.getLoadTypeSeln().equals("6") && user.getRisksSeln() != null && user.getRisksSeln().size() == 0) {
			
			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please select at least one risk !", FlashMessage.Status.FAILURE));
			redirectAttr.addFlashAttribute("user", user);
			return "redirect:/customload";
		}


		final String currentUser = user.getUsername();
		user.setOnlymycards(0);

		User userFromDb;
		if (userService.existsByName(user.getUsername())) {
			long currentUserId = userService.idByName(currentUser);
			user.setId(currentUserId);
			userFromDb = userService.findById(currentUserId);
			addBackUserPrefs(user, userFromDb);
		} else {
			user.setId(null);
		}
		// save selections to Db
		userService.save(user);

		// select cards based on user's search criteria 
		// sortEngineImpl class is in the 'Other' package
		
		List<FCExItem> fc = sortEngine.generateCards(user);

		// warn if nothing found
		if (fc.size() == 0) {
			if (user.getLoadTypeSeln().equals("1")
					|| user.getLoadTypeSeln().equals("4")
					|| user.getLoadTypeSeln().equals("5")
					|| user.getLoadTypeSeln().equals("6")) {

				if (user.getLoadTypeSeln().equals("1")) redirectAttr.addFlashAttribute("flash",
							new FlashMessage("Nothing is found using this keyword !", FlashMessage.Status.FAILURE));
				if (user.getLoadTypeSeln().equals("4")) redirectAttr.addFlashAttribute("flash",
							new FlashMessage("Nothing is found on selected tag(s) !", FlashMessage.Status.FAILURE));
				if (user.getLoadTypeSeln().equals("5")) redirectAttr.addFlashAttribute("flash",
							new FlashMessage("Nothing is found on selected topic(s) !", FlashMessage.Status.FAILURE));
				if (user.getLoadTypeSeln().equals("6")) redirectAttr.addFlashAttribute("flash",
							new FlashMessage("Nothing is found on selected risk(s) !", FlashMessage.Status.FAILURE));
				redirectAttr.addFlashAttribute("user", user);
				return "redirect:/customload";
			}
		}

		return "redirect:/usecards";
	}

	// save user preferences on font size and code view skin on Practice mode
	@PostMapping("/savesettings")
	public String saveUserPreferences(Model model,
			@ModelAttribute("user") User user, BindingResult result,
			RedirectAttributes redirectAttr, Principal principal) {

		final String currentUser = principal.getName();
		user.setUsername(currentUser);

		// since not all user preferences are updated in card selection view 
		// all user preferences need to be merged into one instance of User class 
		User userFromDb;
		if (userService.existsByName(user.getUsername())) {
			long currentUserId = userService.idByName(currentUser);
			user.setId(currentUserId);
			userFromDb = userService.findById(currentUserId);
			addBackUserSelections(user, userFromDb);
		} else {
			// if user is not found in Db setId to null so that a new user is saved in Db
			user.setId(null);
		}

		userService.save(user);
		return "redirect:/usecards";
	}

	// save user preferences on cards with 'Never' status
	@PostMapping(value = "/frequency")
	public String saveCardFrequency(Model model,
			@ModelAttribute("user") User user, BindingResult result,
			RedirectAttributes redirectAttr, Principal principal) {

		final String currentUser = principal.getName();
		user.setUsername(currentUser);

		User userFromDb;
		if (userService.existsByName(user.getUsername())) {
			long currentUserId = userService.idByName(currentUser);
			user.setId(currentUserId);
			userFromDb = userService.findById(currentUserId);
			// since returned 'user' instance contains only updated 'Never' status
			// we add back all other settings back to instance
			addBackAllExceptNevers(user, userFromDb);
		} else {
			// if user is not found in Db setId to null so that a new user is saved in Db
			user.setId(null);
		}

		userService.save(user);
		return "redirect:/usecards";
	}

	private User addBackUserPrefs(User userFromForm, User userFromDb) {

		// User.java contains fields whose values are obtained from different
		// forms (html views).
		// In customselector.html ( used by this Controller class ) user selects
		// only search parameters.
		// In order not to loose values selected by user in other views
		// those other values are added to current user instance before saving
		// the current user's selections.

		// username and usedId is not added here, set current user's username
		// and id before saving

		if (userFromDb.getNeversSeln() != null) {
			userFromForm.setNeversSeln(userFromDb.getNeversSeln());
		}
		if (userFromDb.getSavedSelnCardId() != 0) {
			userFromForm.setSavedSelnCardId(userFromDb.getSavedSelnCardId());
		}
		if (userFromDb.getSavedSeln() != null) {
			userFromForm.setSavedSeln(userFromDb.getSavedSeln());
		}
		if (userFromDb.getSkin() != 0) {
			userFromForm.setSkin(userFromDb.getSkin());
		}
		if (userFromDb.getFontSize() != 0) {
			userFromForm.setFontSize(userFromDb.getFontSize());
		}

		if (userFromDb.getPgDownNotAtBottom() != 0) {
			userFromForm
					.setPgDownNotAtBottom(userFromDb.getPgDownNotAtBottom());
		}

		if (userFromDb.getJavaIde() != null) {
			userFromForm.setJavaIde(userFromDb.getJavaIde());
		}

		return userFromForm;
	}

	private User addBackUserSelections(User userFromForm, User userFromDb) {
		// These are all search parameters
		// search parameters are stored in database in order to restore Updated
		// list of cards upon user's next login

		if (userFromDb.getFramework() != 0) {
			userFromForm.setFramework(userFromDb.getFramework());
		}
		if (userFromDb.getFrameworkSeln() != null) {
			userFromForm.setFrameworkSeln(userFromDb.getFrameworkSeln());
		}

		if (userFromDb.getOnlymycards() != 0) {
			userFromForm.setOnlymycards(userFromDb.getOnlymycards());
		}
		if (userFromDb.getCardTypeSeln() != null) {
			userFromForm.setCardTypeSeln(userFromDb.getCardTypeSeln());
		}
		if (userFromDb.getLoadTypeSeln() != null) {
			userFromForm.setLoadTypeSeln(userFromDb.getLoadTypeSeln());
		}
		if (userFromDb.getRandomNotSeln() != null) {
			userFromForm.setRandomNotSeln(userFromDb.getRandomNotSeln());
		}
		if (userFromDb.getKeywordSeln() != null) {
			userFromForm.setKeywordSeln(userFromDb.getKeywordSeln());
		}
		if (userFromDb.getTopicsSeln() != null) {
			userFromForm.setTopicsSeln(userFromDb.getTopicsSeln());
		}
		if (userFromDb.getRisksSeln() != null) {
			userFromForm.setRisksSeln(userFromDb.getRisksSeln());
		}
		if (userFromDb.getTagsSeln() != null) {
			userFromForm.setTagsSeln(userFromDb.getTagsSeln());
		}
		if (userFromDb.getTagsRule() != null) {
			userFromForm.setTagsRule(userFromDb.getTagsRule());
		}
		if (userFromDb.getTagsMatch() != null) {
			userFromForm.setTagsMatch(userFromDb.getTagsMatch());
		}

		return userFromForm;
	}

	private User addBackAllExceptNevers(User userFromForm, User userFromDb) {

		if (userFromDb.getFramework() != 0) {
			userFromForm.setFramework(userFromDb.getFramework());
		}
		if (userFromDb.getFrameworkSeln() != null) {
			userFromForm.setFrameworkSeln(userFromDb.getFrameworkSeln());
		}
		if (userFromDb.getOnlymycards() != 0) {
			userFromForm.setOnlymycards(userFromDb.getOnlymycards());
		}
		if (userFromDb.getCardTypeSeln() != null) {
			userFromForm.setCardTypeSeln(userFromDb.getCardTypeSeln());
		}
		if (userFromDb.getLoadTypeSeln() != null) {
			userFromForm.setLoadTypeSeln(userFromDb.getLoadTypeSeln());
		}
		if (userFromDb.getRandomNotSeln() != null) {
			userFromForm.setRandomNotSeln(userFromDb.getRandomNotSeln());
		}
		if (userFromDb.getKeywordSeln() != null) {
			userFromForm.setKeywordSeln(userFromDb.getKeywordSeln());
		}
		if (userFromDb.getTopicsSeln() != null) {
			userFromForm.setTopicsSeln(userFromDb.getTopicsSeln());
		}
		if (userFromDb.getRisksSeln() != null) {
			userFromForm.setRisksSeln(userFromDb.getRisksSeln());
		}
		if (userFromDb.getTagsSeln() != null) {
			userFromForm.setTagsSeln(userFromDb.getTagsSeln());
		}
		if (userFromDb.getTagsRule() != null) {
			userFromForm.setTagsRule(userFromDb.getTagsRule());
		}
		if (userFromDb.getTagsMatch() != null) {
			userFromForm.setTagsMatch(userFromDb.getTagsMatch());
		}

		if (userFromDb.getSavedSelnCardId() != 0) {
			userFromForm.setSavedSelnCardId(userFromDb.getSavedSelnCardId());
		}
		if (userFromDb.getSavedSeln() != null) {
			userFromForm.setSavedSeln(userFromDb.getSavedSeln());
		}
		if (userFromDb.getSkin() != 0) {
			userFromForm.setSkin(userFromDb.getSkin());
		}
		if (userFromDb.getFontSize() != 0) {
			userFromForm.setFontSize(userFromDb.getFontSize());
		}

		if (userFromDb.getPgDownNotAtBottom() != 0) {
			userFromForm
					.setPgDownNotAtBottom(userFromDb.getPgDownNotAtBottom());
		}

		if (userFromDb.getJavaIde() != null) {
			userFromForm.setJavaIde(userFromDb.getJavaIde());
		}

		return userFromForm;
	}
}

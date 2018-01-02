package com.jsthf.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsthf.model.FCExItem;
import com.jsthf.model.User;
import com.jsthf.other.FCType;
import com.jsthf.other.FlashMessage;
import com.jsthf.other.Framework;
import com.jsthf.other.SortingEngine;
import com.jsthf.other.Topic;
import com.jsthf.service.FCExItemService;
import com.jsthf.service.RiskService;
import com.jsthf.service.UserService;

// I had better called this class CardsController ( which includes flashcards and example code cards )
@Controller
@PropertySource("classpath:messages.properties")
public class FCExItemController {
	@Autowired
	private FCExItemService fcexitemService;

	@Autowired
	private RiskService riskService;

	@Autowired
	private UserService userService;

	@Autowired
	private SortingEngine sortEngine;

	@Value("${addrule}")
	private String addrule;

	private String currentUser;

	private boolean isUpdate = false;

	// TODO: remove all logs
	private static Logger myLogger = Logger
			.getLogger(FCExItemController.class.getName());

	
	// list of all Cards with info on each
	@GetMapping("/flashcards")
	public String listFCItems(Model model, Principal principal) {

		// in this app class User saves user preferences and UserAuth saves registered users
		User userTemp = getDefaultUserSettings(principal);

		userTemp.setLoadTypeSeln("0");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 0);
		model.addAttribute("heading", "Flashcards:");
		
		return "itemindex";
	}

	// Examples page - index of all FCExItems
	@GetMapping("/examples")
	public String listExItems(Model model, Principal principal) {
		
		User userTemp = getDefaultUserSettings(principal);

		userTemp.setLoadTypeSeln("0");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 1);
		model.addAttribute("heading", "Example code cards:");

		return "itemindex";
	}

	// All cards page - index of all FCExItems
	@GetMapping("/allcards")
	public String listAllItems(Model model, Principal principal) {
		
		User userTemp = getDefaultUserSettings(principal);

		userTemp.setLoadTypeSeln("0");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);
		model.addAttribute("heading", "All cards:");

		return "itemindex";
	}
	
	// All cards added by currently logged in user
	@GetMapping("/mycards")
	public String listAllMyCards(Model model, Principal principal) {

		User userTemp = getDefaultUserSettings(principal);

		userTemp.setOnlymycards(1);
		userTemp.setLoadTypeSeln("0");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);

		model.addAttribute("heading", "Only my cards:");

		return "itemindex";
	}
	
	private Model addDuplicatedToModel(Model model, List<FCExItem> fcs, Principal principal, int criterVal) {
		
		if ( principal != null ) currentUser = principal.getName();
		model.addAttribute("currentuser", currentUser);
		model.addAttribute("frameworks", Framework.values());
		model.addAttribute("topics", Topic.values());
		model.addAttribute("risks", riskService.findAllRisksAddedByUserAndAdmin(currentUser));
		model.addAttribute("tags", sortEngine.getTags(currentUser));
		model.addAttribute("fcexitems", fcs);
		model.addAttribute("criterVal", criterVal);
		model.addAttribute("action", "/keywordsearch");
		
		return model;
	}
	
	private User getDefaultUserSettings(Principal principal) {
		
		if ( principal != null ) currentUser = principal.getName();
		
		User userTemp = new User();
		userTemp.setUsername(currentUser);
		userTemp.setOnlymyframeworks(1);
		userTemp.setOnlymycards(0);
		userTemp.setCardTypeSeln("2");
		userTemp.setRandomNotSeln("1");
		userTemp.setLoadTypeSeln("0");
		
		return userTemp;
	}
	
	// get cards for given framework
	@GetMapping(value = "/fcexitems/{frameworkId}/framework")
	public String getAllForFramework(@PathVariable int frameworkId, Model model, Principal principal) {
		
		User userTemp = getDefaultUserSettings(principal);
		List<String> st = new ArrayList();
		st.add(""+frameworkId);
		userTemp.setFrameworkSeln(st);
		userTemp.setOnlymyframeworks(0);
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		model = addDuplicatedToModel(model, fcs, principal, 2);
		
		model.addAttribute("heading", "All cards for framework: "+Framework.values()[frameworkId].name);
		

		return "itemindex";
	}
	
	// get cards for given topic
	@GetMapping(value = "/fcexitems/{topicId}/topic")
	public String getAllForTopic(@PathVariable int topicId, Model model, Principal principal) {
		
		User userTemp = getDefaultUserSettings(principal);

		List<String> st = new ArrayList();
		st.add(""+topicId);
		userTemp.setTopicsSeln(st);
		userTemp.setLoadTypeSeln("5");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		//myLogger.info("===========>>>>>>>>>>> IN CARD CONTROLLER  sent fcs.size = "+fcs.size());
		model = addDuplicatedToModel(model, fcs, principal, 2);
		model.addAttribute("heading", "All cards for topic: "+Topic.values()[topicId].name);

		return "itemindex";
	}
	
	// get cards for given risk
	@GetMapping(value = "/fcexitems/{riskId}/risk")
	public String getAllForRisk(@PathVariable Long riskId, Model model, Principal principal) {

		User userTemp = getDefaultUserSettings(principal);

		List<String> st = new ArrayList();
		st.add(""+riskId);
		userTemp.setRisksSeln(st);
		userTemp.setLoadTypeSeln("6");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);

		model.addAttribute("heading", "All cards for risk: "+riskService.findById(riskId).getName());

		return "itemindex";
	}
	
	// get cards for given tag
	@GetMapping(value = "/fcexitems/{tagId}/tag")
	public String getAllForATag(@PathVariable int tagId, Model model, Principal principal) {

		User userTemp = getDefaultUserSettings(principal);
		currentUser = principal.getName();
		List<String> st = new ArrayList();
		st.add(sortEngine.getTags(currentUser).get(tagId));
		userTemp.setTagsSeln(st);
		userTemp.setLoadTypeSeln("4");
		userTemp.setTagsMatch("0");
		userTemp.setTagsRule("1");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);

		model.addAttribute("heading", "All cards for tag: "+sortEngine.getTags(currentUser).get(tagId));

		return "itemindex";
	}
	
	// get cards with Never status
	@GetMapping(value = "/neveritems")
	public String getAllNevers(Model model, Principal principal) {

		User userTemp = getDefaultUserSettings(principal);

		userTemp.setLoadTypeSeln("2");
		
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);

		model.addAttribute("heading", "Cards with 'Never' status");

		return "itemindex";
	}
	
	// All cards containing keyword typed
	@GetMapping(value = "/search")
	public String findByKeyword(@RequestParam String qword, Model model, Principal principal) {

		User userTemp = getDefaultUserSettings(principal);

		userTemp.setLoadTypeSeln("1");
		userTemp.setKeywordSeln(qword);
		List<FCExItem> fcs = sortEngine.generateCards(userTemp);
		
		model = addDuplicatedToModel(model, fcs, principal, 2);

		model.addAttribute("heading", "All cards containing the keyword: " + qword);

		return "itemindex";
	}
	
	
	// practice mode
	@GetMapping("/usecards")
	public String practiceCardsJs(Model model, Principal principal) {
		
		final String currentUser = principal.getName();

		User user;
		String isFound = "found";
		String cardsjsn = "";
		
		// in this app class User deals with user preferences and UserAuth deals with registered users
		// checks if user has any saved preferences
		
		if (userService.existsByName(currentUser)) {
			long currentUserId = userService.idByName(currentUser);
			user = userService.findById(currentUserId);
			
			List<FCExItem> fcs = sortEngine.generateCards(user);
			cardsjsn = sortEngine.getCardsInJSONformat(fcs);	
			cardsjsn = cardsjsn.replaceAll("\\<", "&#60;").replaceAll("\\>","&#62;").replaceAll("\\&", "&amp;");
			//myLogger.info("===========>>>>>>>>>>> cardsjsn = "+cardsjsn);
			if (fcs.size() == 0 || fcs.get(0) == null) isFound = "nonefound";  
		} else {
			user = new User();
			isFound = "nonefound";
		}
	
		model.addAttribute("nonefound", isFound);
		model.addAttribute("cardsjsn", cardsjsn);
		model.addAttribute("user", user);
		model.addAttribute("frameworksjsn", sortEngine.getFrameworksInJSONformat());
		model.addAttribute("actionS", "/savesettings");
		model.addAttribute("actionF", "/frequency");
		model.addAttribute("isNewSelection", sortEngine.isNewSelection());
		sortEngine.setIsNewSelection(false);

		return "itemuse";
	}



	// Single FCExItem page
	@GetMapping("/fcexitem/{fcexitemId}")
	public String fcexitemDetails(@PathVariable Long fcexitemId, Model model) {
		FCExItem fcexitem = fcexitemService.findById(fcexitemId);
		model.addAttribute("fcexitem", fcexitem);
		model.addAttribute("topics", Topic.values());
		return "itemdetails";
	}

	// START OF ADD PART

	// Form for adding a new FCExItem
	@GetMapping("/addfc")
	public String formNewFCExItem(Model model, Principal principal) {

		currentUser = principal.getName();
		
		model = sortEngine.addPresetCard(model, currentUser);
		model = sortEngine.addFramework(model, currentUser);

		model = addDuplicatedToModelForForm(model, currentUser);

		model.addAttribute("heading", "Add new card");
		model.addAttribute("submit", "Submit");

		isUpdate = false;
		return "itemform";
	}

	private Model addDuplicatedToModelForForm(Model model, String currentUser) {

		String topicsjsn = sortEngine.getTopicsInJSONformat();

		String risksjsn = sortEngine.getRisksInJSONformat(currentUser);

		model.addAttribute("currentuser", currentUser);
		model.addAttribute("fctypes", FCType.values());
		model.addAttribute("frameworks", Framework.values());
		model.addAttribute("topics", Topic.values());
		model.addAttribute("topicsjsn", topicsjsn);
		model.addAttribute("risks", riskService.findAllRisksAddedByUserAndAdmin(currentUser));
		model.addAttribute("risksjsn", risksjsn);
		model.addAttribute("addrule", addrule);
		model.addAttribute("action", "/addfc");
		model.addAttribute("begincolumn", "<tr>");
		model.addAttribute("endcolumn", "</tr>");

		return model;
	}
	
	// Add a new FCExItem ( ACTUALLY ADD TO DB )
	@PostMapping(value = "/addfc")
	public String addFCExItem(@Valid FCExItem fcexitem, BindingResult result, RedirectAttributes redirectAttr) {
 
		if (result.hasErrors()) {redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.fcexitem",result);

			redirectAttr.addFlashAttribute("fcexitem", fcexitem);

			return "redirect:/addfc";
		}

		if (fcexitem.getFrameworkSeln().size() == 0) {

			redirectAttr.addFlashAttribute("fcexitem", fcexitem);

			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please select a framework !", FlashMessage.Status.FAILURE));

			return "redirect:/addfc";
		}

		if (fcexitem.getTopic().size() == 0) {

			redirectAttr.addFlashAttribute("fcexitem", fcexitem);

			redirectAttr.addFlashAttribute("flash", new FlashMessage("Please select a topic !", FlashMessage.Status.FAILURE));

			return "redirect:/addfc";
		}

		if (!isUpdate) {
			// add default 'no risk' risk if user did not select any
			if (fcexitem.getRisk() == null) fcexitem.setRisk(riskService.findById(1L));
			fcexitemService.save(fcexitem);
			redirectAttr.addFlashAttribute("flash", new FlashMessage("Card successfully added !", FlashMessage.Status.SUCCESS));
		} else {
			if (currentUser != null && fcexitem.getUser() != null && currentUser.equals(fcexitem.getUser())) {
				fcexitemService.save(fcexitem);
				redirectAttr.addFlashAttribute("flash", new FlashMessage("Card successfully updated !", FlashMessage.Status.SUCCESS));
			} else {
				redirectAttr.addFlashAttribute("flash", new FlashMessage("You do not have edit rights on this card !", FlashMessage.Status.FAILURE));
				return "redirect:/allcards";
			}
		}

		return "redirect:/allcards";
	}

	// END OF ADD PART

	// START OF EDIT PART
	// Form for editing an existing FCExItem
	@GetMapping("/fcexitems/{fcexitemId}/edit")
	public String formEditFCExItem(@PathVariable Long fcexitemId, Model model, Principal principal) {

		currentUser = principal.getName();

		if (!model.containsAttribute("fcexitem")) {
			model.addAttribute("fcexitem", fcexitemService.findById(fcexitemId));
		}

		model = addDuplicatedToModelForForm(model, currentUser);
		model.addAttribute("heading", "Edit Card");
		model.addAttribute("submit", "Update");

		isUpdate = true;
		return "itemform";
	}

	// Delete an existing FCExItem
	@PostMapping(value = "/fcexitems/{fcexitemId}/delete")
	public String deleteFCExItem(@PathVariable Long fcexitemId,
			RedirectAttributes redirectAttributes) {

		// Delete the FCExItem whose id is fcexitemId
		FCExItem fcexitem = fcexitemService.findById(fcexitemId);

		if (currentUser != null && fcexitem.getUser() != null && currentUser.equals(fcexitem.getUser())) {
			fcexitemService.delete(fcexitem);
			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"Card successfully deleted!", FlashMessage.Status.SUCCESS));
			return "redirect:/allcards";
		} else {
			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"You do not have edit rights on this card !", FlashMessage.Status.FAILURE));
			return "redirect:/allcards";
		}

	}

}


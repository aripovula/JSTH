package com.jsthf.controller;

import java.security.Principal;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsthf.model.Risk;
import com.jsthf.other.FlashMessage;
import com.jsthf.other.Framework;
import com.jsthf.other.SortingEngine;
import com.jsthf.other.Topic;
import com.jsthf.service.RiskService;
import com.jsthf.service.UserService;

@Controller
public class RiskController {
	@Autowired
	private RiskService riskService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SortingEngine sortEngine;


	private String currentUser;

	private static Logger myLogger = Logger
			.getLogger(FCExItemController.class.getName());

	// Index of all risks
	@GetMapping("/risks")
	public String listRisks(Model model, Principal principal) {

		currentUser = principal.getName();
		
		model.addAttribute("currentuser", currentUser);
		model.addAttribute("risks", riskService.findAllRisksAddedByUserAndAdmin(currentUser));
		
		return "riskindex";
	}

	// Single risk page
	@GetMapping("/risks/{riskId}")
	public String risk(@PathVariable Long riskId, Model model) {

		Risk risks = riskService.findById(riskId);

		model.addAttribute("risk", risks);
		model.addAttribute("topics", Topic.values());
		return "riskdetails";
	}

	// Form for adding a new risk
	@GetMapping("risks/add")
	public String formNewRisk(Model model, Principal principal) {

		currentUser = principal.getName();
		
		model = sortEngine.addPresetRisk(model, currentUser);
		model = sortEngine.addFramework(model, currentUser);
		String topicsjsn = sortEngine.getTopicsInJSONformat();

		model.addAttribute("frameworks", Framework.values());
		model.addAttribute("topics", Topic.values());
		model.addAttribute("topicsjsn", topicsjsn);
		model.addAttribute("currentuser", currentUser);
		model.addAttribute("action", "/risks");
		model.addAttribute("heading", "New Risk");
		model.addAttribute("submit", "Add");

		return "riskform";
	}

	// Add a risk to DB
	@PostMapping("/risks")
	public String addRisk(@Valid Risk risk, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {

			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.risk", result);

			redirectAttributes.addFlashAttribute("risk", risk);

			return "redirect:/risks/add";
		}

		// if no topic is added to a risk
		if (risk.getTopic().size() == 0) {
			redirectAttributes.addFlashAttribute("risk", risk);

			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"Please select a topic !", FlashMessage.Status.FAILURE));

			return "redirect:/risks/add";
		}

		risk.setUser(currentUser);
		riskService.save(risk);

		redirectAttributes.addFlashAttribute("flash", new FlashMessage("Risk successfully added !", FlashMessage.Status.SUCCESS));

		return "redirect:/risks";
	}

	// Form for editing an existing risk
	@GetMapping("risks/{riskId}/edit")
	public String formEditRisk(@PathVariable Long riskId, Model model, Principal principal) {

		if (!model.containsAttribute("risk")) {
			model.addAttribute("risk", riskService.findById(riskId));
		}

		currentUser = principal.getName();

		model = sortEngine.addFramework(model, currentUser);		
		
		String topicsjsn = sortEngine.getTopicsInJSONformat();

		model.addAttribute("frameworks", Framework.values());
		model.addAttribute("topics", Topic.values());
		model.addAttribute("topicsjsn", topicsjsn);
		model.addAttribute("currentuser", currentUser);
		model.addAttribute("action", String.format("/risks/%s", riskId));
		model.addAttribute("heading", "Edit Risk");
		model.addAttribute("submit", "Update");

		return "riskform";
	}

	// Update an existing risk
	@PostMapping(value = "/risks/{riskId}")
	public String updateRisk(@Valid Risk risk, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.risk", result);

			redirectAttributes.addFlashAttribute("risk", risk);

			return String.format("redirect:/risks/%s/edit", risk.getId());
		}

		// in case all topics were un-ticked
		if (risk.getTopic().size() == 0) {

			redirectAttributes.addFlashAttribute("risk", risk);

			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"Please select a topic !", FlashMessage.Status.FAILURE));

			return "redirect:/risks/add";
		}
		
		if (currentUser != null && risk.getUser() != null && currentUser.equals(risk.getUser())) {
			riskService.save(risk);
			redirectAttributes.addFlashAttribute("flash", new FlashMessage("Risk successfully updated !", FlashMessage.Status.SUCCESS));
		} else {
			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"You do not have edit rights on this risk !", FlashMessage.Status.FAILURE));
		}

		return "redirect:/risks";
	}

	// Delete an existing risk
	@PostMapping("/risks/{riskId}/delete")
	public String deleteRisk(@PathVariable Long riskId, RedirectAttributes redirectAttributes, Model model) {
		Risk rsk = riskService.findById(riskId);
		model.addAttribute("risk", rsk);
		// Delete risk if it contains no cards
		if (rsk.getCards().size() > 0) {
			redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only risks with no associated cards can be deleted !", FlashMessage.Status.FAILURE));
			return String.format("redirect:/risks/%s/edit", riskId);
		}
		
		// to prevent unauthorized deletion
		if (currentUser != null && rsk.getUser() != null && currentUser.equals(rsk.getUser())) {
			riskService.delete(rsk);
			redirectAttributes.addFlashAttribute("flash", new FlashMessage("Risk deleted !", FlashMessage.Status.SUCCESS));			
		} else {
			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"You do not have edit rights on this risk !", FlashMessage.Status.FAILURE));
		}

		return "redirect:/risks";
	}
}

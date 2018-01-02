package com.jsthf.other;

import java.util.List;

import org.springframework.ui.Model;

import com.jsthf.model.FCExItem;
import com.jsthf.model.User;

public interface SortingEngine {

	public List<FCExItem> generateCards(User user);

	public List<String> getTags(String username);

	public void setIsNewSelection(boolean isNewS);

	public boolean isNewSelection();

	public String getCardsInJSONformat(List<FCExItem> fcs);

	public String getFrameworksInJSONformat();
	
	public String getTopicsInJSONformat();

	public String getRisksInJSONformat(String username);

	public String getSourceCodeText();

	public String getSourceCodeFromFile(String string);
	
	public Model addPresetCard(Model model, String currentUser);
	
	public Model addPresetRisk(Model model, String currentUser);
	
	public Model addFramework(Model model, String currentUser);


}

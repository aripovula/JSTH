package com.jsthf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jsthf.other.StringListConverter;

// This class is intended to manage User preferences while class UserAuth is used to manage signed users

@Entity
@Table(name = "user_prefs")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private int framework;

	// this one is used for custom selection - can select more than one
	@Column(name = "frameworkSeln", columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> frameworkSeln = new ArrayList<>();

	private int onlymyframeworks;
	
	private int onlymycards;

	// last search parameters are saved in order to restore
	// the selection upon next login when session ends
	private int currentCardId;

	private String cardTypeSeln;

	private String loadTypeSeln;

	private String randomNotSeln;

	private String keywordSeln;

	@Convert(converter = StringListConverter.class)
	private List<String> topicsSeln = new ArrayList<>();

	@Column(name = "risksSeln", columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> risksSeln = new ArrayList<>();

	@Column(name = "tagsSeln", columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> tagsSeln = new ArrayList<>();

	private String tagsRule;

	private String tagsMatch;

	// list of cards user does not want to practice again
	@Column(name = "neversSeln", columnDefinition = "TEXT")
	private String neversSeln;

	// if user wants to save specific selection it is saved to following two
	// fields and can be restored anytime until overwritten
	private int savedSelnCardId;

	@Column(name = "savedSeln", columnDefinition = "LONGTEXT")
	private String savedSeln;

	// user's view and other preferences
	private int skin;

	private int fontSize;

	private int pgDownNotAtBottom;

	private String javaIde;

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getCurrentCardId() {
		return currentCardId;
	}

	public void setCurrentCardId(int currentCardId) {
		this.currentCardId = currentCardId;
	}

	public int getFramework() {
		return framework;
	}

	public void setFramework(int framework) {
		this.framework = framework;
	}

	public List<String> getFrameworkSeln() {
		return frameworkSeln;
	}

	public void setFrameworkSeln(List<String> frameworkSeln) {
		this.frameworkSeln = frameworkSeln;
	}

	public int getOnlymyframeworks() {
		return onlymyframeworks;
	}

	public void setOnlymyframeworks(int onlymyframeworks) {
		this.onlymyframeworks = onlymyframeworks;
	}

	public int getOnlymycards() {
		return onlymycards;
	}

	public void setOnlymycards(int onlymycards) {
		this.onlymycards = onlymycards;
	}

	public String getCardTypeSeln() {
		return cardTypeSeln;
	}

	public void setCardTypeSeln(String cardTypeSeln) {
		this.cardTypeSeln = cardTypeSeln;
	}

	public String getLoadTypeSeln() {
		return loadTypeSeln;
	}

	public void setLoadTypeSeln(String loadTypeSeln) {
		this.loadTypeSeln = loadTypeSeln;
	}

	public String getRandomNotSeln() {
		return randomNotSeln;
	}

	public void setRandomNotSeln(String randomNotSeln) {
		this.randomNotSeln = randomNotSeln;
	}

	public String getKeywordSeln() {
		return keywordSeln;
	}

	public void setKeywordSeln(String keywordSeln) {
		this.keywordSeln = keywordSeln;
	}

	public List<String> getTopicsSeln() {
		return topicsSeln;
	}

	public void setTopicsSeln(List<String> topicsSeln) {
		this.topicsSeln = topicsSeln;
	}

	public List<String> getRisksSeln() {
		return risksSeln;
	}

	public void setRisksSeln(List<String> risksSeln) {
		this.risksSeln = risksSeln;
	}

	public List<String> getTagsSeln() {
		return tagsSeln;
	}

	public void setTagsSeln(List<String> tagsSeln) {
		this.tagsSeln = tagsSeln;
	}

	public String getTagsRule() {
		return tagsRule;
	}

	public void setTagsRule(String tagsRule) {
		this.tagsRule = tagsRule;
	}

	public String getTagsMatch() {
		return tagsMatch;
	}

	public void setTagsMatch(String tagsMatch) {
		this.tagsMatch = tagsMatch;
	}

	public String getNeversSeln() {
		return neversSeln;
	}

	public void setNeversSeln(String neversSeln) {
		this.neversSeln = neversSeln;
	}

	public int getSavedSelnCardId() {
		return savedSelnCardId;
	}

	public void setSavedSelnCardId(int savedSelnCardId) {
		this.savedSelnCardId = savedSelnCardId;
	}

	public String getSavedSeln() {
		return savedSeln;
	}

	public void setSavedSeln(String savedSeln) {
		this.savedSeln = savedSeln;
	}

	public int getSkin() {
		return skin;
	}

	public void setSkin(int skin) {
		this.skin = skin;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getPgDownNotAtBottom() {
		return pgDownNotAtBottom;
	}

	public void setPgDownNotAtBottom(int pgDownNotAtBottom) {
		this.pgDownNotAtBottom = pgDownNotAtBottom;
	}

	public String getJavaIde() {
		return javaIde;
	}

	public void setJavaIde(String javaIde) {
		this.javaIde = javaIde;
	}

}

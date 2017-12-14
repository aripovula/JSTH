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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jsthf.other.StringListConverter;

@Entity
@Table(name = "risk")
public class Risk implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 255, message = "Please add risk description with up to 255 characters")
	private String name;

	@Convert(converter = StringListConverter.class)
	private List<String> topic = new ArrayList<>();

	@OneToMany(mappedBy = "risk")
	private List<FCExItem> cards = new ArrayList<>();

	private int frameworkId;

	// default - all users - only a user has access if he/she added it
	@Column(name = "user", columnDefinition = "varchar(255) default 'a'")
	private String user;

	public Risk() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTopic() {
		return topic;
	}

	public void setTopic(List<String> topic) {
		this.topic = topic;
	}

	public List<FCExItem> getCards() {
		return cards;
	}

	public void setCards(List<FCExItem> cards) {
		this.cards = cards;
	}

	public int getFrameworkId() {
		return frameworkId;
	}

	public void setFrameworkId(int frameworkId) {
		this.frameworkId = frameworkId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
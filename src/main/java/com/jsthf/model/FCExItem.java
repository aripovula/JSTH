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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jsthf.other.StringListConverter;


// FCExItem - meaning Flashcard + ExampleCode Items
// I should have called this class 'Card'
@Entity
@Table(name = "fcex_item")
public class FCExItem implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, message = "Please add question text")
    @Lob
    private String first;

    @NotNull
    @Size(min = 1, message = "Please add answer text")
    @Lob
    private String second;
  
    @NotNull(message = "Please select a topic")
    @Column(name = "topic", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> topic = new ArrayList<>();

    @NotNull(message = "Please select card type")
    private String type;
    
    @Size(max = 255, message = "Should be less than 255 characters")
    private String tag;

    @NotNull(message = "Please select a framework")
    @Column(name = "frameworkSeln", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> frameworkSeln = new ArrayList<>();

    @ManyToOne
    private Risk risk;

    //default - all users - only a user has access if he/she added it
    @Column(name = "user", columnDefinition = "varchar(255) default 'a'")
    private String user;

    public FCExItem(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    
    public List<String> getTopic() {
		return topic;
	}

	public void setTopic(List<String> topic) {
		this.topic = topic;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<String> getFrameworkSeln() {
		return frameworkSeln;
	}

	public void setFrameworkSeln(List<String> frameworkSeln) {
		this.frameworkSeln = frameworkSeln;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }
   
}
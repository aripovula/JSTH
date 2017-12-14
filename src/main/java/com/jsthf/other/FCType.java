package com.jsthf.other;

public enum FCType {
    FLASHCARDS (0, "Flashcards"), EXAMPLE_CODE (1, "Example code");
    
	public int id;
	public String name;

    private FCType (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}



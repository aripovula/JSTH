package com.jsthf.other;

public enum TypeOptions {
    FLASHCARDS    (0, "Flashcards"), 
    EXAMPLE_CODE  (1, "Example code"), 
    ALL_CARDS     (2, "Flashcards + Example code");
    
	public int id;
	public String name;

    private TypeOptions (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}



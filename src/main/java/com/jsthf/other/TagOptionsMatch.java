package com.jsthf.other;

public enum TagOptionsMatch {
    EXACT    (0, "Only exact match"), 
    CONTAIN  (1, "Partial match");
    
	public int id;
	public String name;

    private TagOptionsMatch (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}



package com.jsthf.other;

public enum TagOptionsRule {
    AMD    (0, "Cards with tag1 AND tag2"), 
    OR  (1, "Cards with tag1 OR tag2");
    
	public int id;
	public String name;

    private TagOptionsRule (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}



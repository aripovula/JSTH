package com.jsthf.other;

public enum SortOptions {
    RANDOM (0, "Re-sort randomly"), 
    ASC    (1, "Re-sort by ID (asc.)"), 
    DESC   (2, "Re-sort by ID (desc.)");
    
	public int id;
	public String name;

    private SortOptions (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}



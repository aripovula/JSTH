package com.jsthf.other;

public enum LoadOptions {

	LOAD0 (0, "Load all"),
	LOAD1 (1, "Load ones with custom keyword"), 
	LOAD2 (2, "Load ones with 'Never' status"),
	LOAD3 (3, "Load ones with no tag"), 
	LOAD4 (4, "Load ones with selected tag(s)"),
	LOAD5 (5, "Load ones with selected topic(s)"),
	LOAD6 (6, "Load ones with selected risk(s)");

	public int id;
	public String name;

    private LoadOptions (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }   
}

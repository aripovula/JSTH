package com.jsthf.other;

public enum Framework {
    JAVAOCA(0, "Java OCA - 1z0-808"),
    JAVAOCP(1, "Java OCP - 1z0-809"),
    SERVLETS(2,"Servlet / JSP / JSTL"),
    SPRING(3, "Spring"),
    HIBERNATE(4, "Hibernate"),
    THYMELEAF(5, "Thymeleaf");

	public int id;
	public String name;

    private Framework (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }
}


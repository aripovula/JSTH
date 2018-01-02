package com.jsthf.other;

public enum Topic {
    OCABASICS(0, "Java basics", "Java OCA - 1z0-808"),
    DATATYPES(1, "Working with Java data types", "Java OCA - 1z0-808"),
    ENCAPS(2, "Methods and encapsulation", "Java OCA - 1z0-808"),
    ARRAYS(3, "Selected classes from the Java API and arrays", "Java OCA - 1z0-808"),
    FLOW(4, "Flow control", "Java OCA - 1z0-808"),
    INHERITANCE(5, "Working with inheritance", "Java OCA - 1z0-808"),
    EXCEPTIONS(6, "Exception handling", "Java OCA - 1z0-808"),
    BASICS(7, "Basics - Java Class Design & etc.", "Java OCP - 1z0-809"),
    DESIGNPS(8,"Advanced Java Class Design", "Java OCP - 1z0-809"),
    GnC(9,"Generics and Collections", "Java OCP - 1z0-809"),
    STREAMS(10, "Functional Interfaces + Stream API", "Java OCP - 1z0-809"),
    TRY(11, "Exceptions and Assertions", "Java OCP - 1z0-809"),
    DateTimeLocale(12, "Date/Time API + Locale", "Java OCP - 1z0-809"),
    IO(13, "Java I/O", "Java OCP - 1z0-809"),
    NIO2(14, "NIO.2", "Java OCP - 1z0-809"),
    Concurrency(15, "Concurrency", "Java OCP - 1z0-809"),
    JDBC(16, "JDBC", "Java OCP - 1z0-809"),
    OTHER(17, "Other", "Java OCP - 1z0-809"),
    SCORE(18, "Core", "Spring"),
    SMVC(19, "MVC", "Spring"),
    SBOOT(20, "BOOT", "Spring"),
    SAOP(21, "AOP", "Spring"),
    SSECURITY(22, "Security", "Spring"),
    TMVC(23, "General MVC", "Thymeleaf"),
    TSMVC(24, "Spring MVC", "Thymeleaf"),
    HXML(25, "Using Native Hibernate APIs", "Hibernate"),
    HANNOT(26, "Using Annotation Mappings", "Hibernate"),
    JPA(27, "Using the Java Persistence API (JPA)", "Hibernate"),
    HENVERS(28, "Using Envers", "Hibernate"),
	SERVLETS(29, "Servlets", "Servlet / JSP / JSTL"),
	JSP(30, "JSP", "Servlet / JSP / JSTL"),
	JSTL(31, "JSTL", "Servlet / JSP / JSTL");

	public int id;
	public String name;
	public String framework;

    private Topic (int id, String name, String framework){
	    	this.id = id;    
	    	this.name = name;
	    	this.framework = framework;
    }
}


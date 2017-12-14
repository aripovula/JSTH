package com.jsthf.other;

public enum OriginOptions {
	ALLCARDS(0, "All cards"), ONLYMY(2, "Cards added by me only");

	public int id;
	public String name;

	private OriginOptions(int id, String name) {
		this.id = id;
		this.name = name;
	}
}

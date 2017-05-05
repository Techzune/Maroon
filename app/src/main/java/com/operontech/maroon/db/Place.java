package com.operontech.maroon.db;

public class Place {
	private String id;
	private String title;
	private String description;

	public void setID(final String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}

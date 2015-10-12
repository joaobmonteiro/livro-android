package com.codeslashers.catalog;

public class Book {

	private String title;
	private String author;
	private int image;
	
	public Book(String title, String author, int image) {
		this.title = title;
		this.author = author;
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

}

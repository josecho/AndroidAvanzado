package com.example.audiolibros;

public class BookInfo {
	public String name;
	public String autor;
	public int resourceImage;
	public String url;
	public BookInfo(String name, String autor, int resourceImage,
														String url) {
		this.name = name;
		this.autor = autor;
		this.resourceImage = resourceImage;
		this.url = url;
	}
}

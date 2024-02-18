package com.bookStore.entity;

import org.springframework.boot.context.properties.bind.Name;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class book {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;//book name
	private String author;//author name
	private String price;
	
	private String bookImageName;//book image name  
//	bookImageName === coverImageFileName

    private String bookDescription;
    
    private String bookType;


	public String getBookType() {
		return bookType;
	}



	public void setBookType(String bookType) {
		this.bookType = bookType;
	}



	public book() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getBookImageName() {
		return bookImageName;
	}



	public void setBookImageName(String bookImageName) {
		this.bookImageName = bookImageName;
	}



	public String getBookDescription() {
		return bookDescription;
	}



	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}



	public String getbookImageName() {
		return bookImageName;
	}



	public void setbookImageName(String bookImageName) {
		this.bookImageName = bookImageName;
	}





	public book(int id, String name, String author, String price, String bookImageName, String bookDescription,
			String bookType) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.price = price;
		this.bookImageName = bookImageName;
		this.bookDescription = bookDescription;
		this.bookType = bookType;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}



}

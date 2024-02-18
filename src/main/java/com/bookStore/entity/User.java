package com.bookStore.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String userName;

	private String email;

	private String mobileNo;

	private String password;

	private String role;
	
	private boolean enable;

	private String verificationCode;
	
	private String profileImage;
	
	
// add to favorite code start
	
	 @ManyToMany(cascade = CascadeType.MERGE)
	 @JoinTable(name = "user_favorite_books",
     joinColumns = @JoinColumn(name = "user_id"),
     inverseJoinColumns = @JoinColumn(name = "book_id"))
	    private List<book> favoriteBooks;
	
	
 
	public List<book> getFavoriteBooks() {
		return favoriteBooks;
	}

	public void setFavoriteBooks(List<book> favoriteBooks) {
		this.favoriteBooks = favoriteBooks;
	}

	// add to favorite code end
	
	
	
	
	
	
	public boolean isEnable() {
		return enable;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

//	public User() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
// 
//	
//
//	
//
//	public User(int id, String name, String userName, String email, String mobileNo, String password, String role,
//			boolean enable, String verificationCode) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.userName = userName;
//		this.email = email;
//		this.mobileNo = mobileNo;
//		this.password = password;
//		this.role = role;
//		this.enable = enable;
//		this.verificationCode = verificationCode;
//	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", userName=" + userName + ", email=" + email + ", mobileNo="
				+ mobileNo + ", password=" + password + ", role=" + role + ", enable=" + enable + ", verificationCode="
				+ verificationCode + ", profileImage=" + profileImage + ", favoriteBooks=" + favoriteBooks + "]";
	}







}

package com.booknest.model;

public class UserModel {

	private int id;

	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String email;
	private String phoneNumber;
	private String address;
	private String userImgUrl;
	private int roleID;
	private String roleName; // Added to store the user's role name

	// Constructor with all fields
	public UserModel(String firstName, String lastName, String userName, String password, String email,
			String phoneNumber, String address, int roleID, String roleName, String userImgUrl) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.roleID = roleID;
		this.roleName = roleName;
		this.userImgUrl = userImgUrl;
	}

	public UserModel(String firstName, String lastName, String userName, String password, String email,
			String phoneNumber, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;

//		this.user_img_url = user_img_url;
	}

	// Constructor for minimal fields
	public UserModel(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	// Constructor for roleID only
	public UserModel(int roleID) {
		this.roleID = roleID;
	}

	// Getters and Setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserImgUrl() {
		return userImgUrl;
	}

	public void setUserImgUrl(String UserImgUrl) {
		this.userImgUrl = UserImgUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleID;
	}

	public void setRoleId(int roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
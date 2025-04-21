package com.booknest.model;

public class UserModel {
    private String first_name;
    private String last_name;
    private String user_name;
    private String password;
    private String email;
    private String phone_number;
    private String address;
    private String user_img_url;
    private int roleID;
    private String roleName; // Added to store the user's role name

    // Constructor with all fields
    public UserModel(String first_name, String last_name, String user_name, String password, String email,
                     String phone_number, String address, int roleID, String roleName, String user_img_url) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.roleID = roleID;
        this.roleName = roleName;
        this.user_img_url = user_img_url;
    }
	public UserModel(String first_name, String last_name,String user_name, String password, String email, String phone_number,
			String address) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_name = user_name;
		this.password = password;
		this.email = email;
		this.phone_number = phone_number;
		this.address = address;
		
//		this.user_img_url = user_img_url;
	}

    // Constructor for minimal fields
    public UserModel(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    // Constructor for roleID only
    public UserModel(int roleID) {
        this.roleID = roleID;
    }

    // Getters and Setters
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_img_url() {
        return user_img_url;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
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
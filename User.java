//package com.RideShare;

public  class User {
	
	    private int id;
	    private String name;
	    private String email;
	    private String password;
	    private String role;

	    // Constructor
	    public User(int id, String name, String email, String password, String role) {
	        this.id = id;
	        this.name = name;
	        this.email = email;
	        this.password = password;
	        this.role = role;
	    }

	    // Getters
	    public int getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getPassword() {
	        return password; // Add this getter method
	    }

	    public String getRole() {
	        return role;
	    }
	

    public static User login(String email, String password) {
        UserDAO userDAO = new UserDAO();
        if (userDAO.checkUserCredentials(email, password)) {
            return userDAO.getUserByEmail(email);
        }
        return null;
    }
}


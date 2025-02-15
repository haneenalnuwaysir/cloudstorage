package org.springframework.boot.cloudstorage.model;

public class User {
    private Integer userId;
    private String userName;
    private String salt;
    private String password;
    private String firstName;
    private String lastName;

    public User(Integer userId, String userName, String salt, String password, String firstName, String lastName) {
        this.userId = userId;
        this.userName = userName;
        this.salt = salt;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        if (userName.length() > 20) return userName.substring(0, 20);
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        if (firstName.length() > 20) return firstName.substring(0, 20);
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (lastName.length() > 20) return lastName.substring(0, 20);
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

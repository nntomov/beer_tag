package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "login_user_info")
public class LoginUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LoginID")
    private int id;

    @Column(name = "Username")
    @Size(min = 2, max = 45, message = "Username size must be between 2 and 45.")
    private String username;

    @Column(name = "Password")
    @Size(min = 5, max = 127, message = "Password size must be between 2 and 127.")
    private String password;

    public LoginUserInfo() {
    }

    public LoginUserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_beer_type")
public class UserBeerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserBeerTypeID")
    private int id;

    @NotNull
    @Column(name = "Type")
    @Size(max = 15, message = "Incorrect size for beer type")
    private String type;

    public UserBeerType() {

    }

    public UserBeerType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

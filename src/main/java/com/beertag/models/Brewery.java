package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "breweries")
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BreweryID")
    private int id;

    @Column(name = "BreweryName")
    @Size(min = 5, max = 45, message = "Incorrect size for BreweryName")
    private String name;

    public Brewery() {}

    public Brewery(String name) {
        this.name = name;
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
}

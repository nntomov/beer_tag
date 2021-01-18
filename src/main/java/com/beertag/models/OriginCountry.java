package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "countries")
public class OriginCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CountryID")
    private int id;

    @NotNull
    @Column(name = "CountryCode")
    private String countryCode;

    @NotNull
    @Column(name = "CountryName")
    @Size(max = 100, message = "Incorrect size for BreweryName")
    private String name;

    public OriginCountry() {}

    public OriginCountry(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

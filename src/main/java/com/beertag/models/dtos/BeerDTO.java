package com.beertag.models.dtos;

import org.springframework.web.multipart.MultipartFile;

public class BeerDTO {

    private String name;

    private String addedBy;

    private String breweryProducer;

    private String originCountry;

    private double ABV;

    private String description;

    private String style;

    private MultipartFile picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBreweryProducer() {
        return breweryProducer;
    }

    public void setBreweryProducer(String breweryProducer) {
        this.breweryProducer = breweryProducer;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public double getABV() {
        return ABV;
    }

    public void setABV(double ABV) {
        this.ABV = ABV;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}

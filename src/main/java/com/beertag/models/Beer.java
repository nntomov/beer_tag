package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "beers")
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BeerID")
    private int id;

    @Column(name = "BeerName")
    @Size(min = 2, max = 30, message = "Incorrect size for BeerName")
    private String name;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User addedBy;

    @Column(name = "BreweryName")
    private String breweryProducer;

    @ManyToOne
    @JoinColumn(name = "CountryID")
    private OriginCountry originCountry;

    @Column(name = "AlcoholByVolume")
    @PositiveOrZero
    private double ABV;

    @Column(name = "Description")
    @Size(max = 255, message = "Description can not exceed 255 characters")
    private String description;

    @ManyToOne
    @JoinColumn(name = "StyleID")
    private BeerStyle style;

    @Column(name = "PictureData")
    @NotNull
    private byte[] picture;

    @OneToMany(mappedBy = "beer", fetch = FetchType.EAGER)
    private Set<Rating> ratings;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "beer_tag_relations",
            joinColumns = @JoinColumn(name = "BeerID"),
            inverseJoinColumns = @JoinColumn(name = "TagID")
    )
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "BeerID")
    private Set<BeerTypeStatus> bts;

    @Column(name = "AvgRating")
    private double avgRating;

    public Beer() {
    }

    public Beer(String name, String breweryProducer, OriginCountry originCountry,
                double ABV, String description, BeerStyle beerStyle, byte[] picture, User addedBy) {
        ratings = new HashSet<>();
        tags = new ArrayList<>();
        this.name = name;
        this.breweryProducer = breweryProducer;
        this.originCountry = originCountry;
        this.ABV = ABV;
        this.description = description;
        this.style = beerStyle;
        this.picture = picture;
        this.addedBy = addedBy;
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

    public String getBreweryProducer() {
        return breweryProducer;
    }

    public void setBreweryProducer(String breweryProducer) {
        this.breweryProducer = breweryProducer;
    }

    public OriginCountry getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(OriginCountry originCountry) {
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

    public BeerStyle getStyle() {
        return style;
    }

    public void setStyle(BeerStyle beerStyle) {
        this.style = beerStyle;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public Set<BeerTypeStatus> getBts() {
        return bts;
    }

    public void setBts(Set<BeerTypeStatus> bts) {
        this.bts = bts;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }

    public void updateRating(Rating rating) {
        Rating updated = getRatings().stream().filter(r -> r.getUser().getId() == rating.getUser().getId() &&
                r.getBeer().getId() == rating.getBeer().getId()).findFirst().get();

        ratings.remove(updated);
        ratings.add(rating);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}

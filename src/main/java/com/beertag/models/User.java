package com.beertag.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int id;

    @Column(name = "AvatarData")
    @NotNull
    private byte[] photoData;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password", length = 45)
    private String password;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private Set<Rating> ratings;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private List<Beer> beerList;

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] getPhoto() {
        return photoData;
    }

    public void setPhoto(byte[] photoData) {
        this.photoData = photoData;
    }

    public void addRating(Rating rating){
        ratings.add(rating);
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public void updateRating(Rating rating) {
       Rating updated = getRatings().stream().filter(r -> r.getBeer().getId() == rating.getBeer().getId() &&
                r.getUser().getId() == rating.getUser().getId()).findFirst().get();

        ratings.remove(updated);
        ratings.add(rating);
    }

    public void addBeer(Beer beer){
        beerList.add(beer);
    }

    public List<Beer> getBeerList() {
        return beerList;
    }

    public void setBeerList(List<Beer> beerList) {
        this.beerList = beerList;
    }

}

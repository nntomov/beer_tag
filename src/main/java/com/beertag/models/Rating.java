package com.beertag.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "beer_ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RatingID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "BeerID")
    @JsonIgnore
    private Beer beer;

    @Max(10)
    @Min(1)
    @Column(name = "Rating")
    private int rating;

    public Rating() {
    }

    public Rating( int rating,User user,Beer beer) {
        setUser(user);
        setBeer(beer);
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

package com.beertag.models;

import javax.persistence.*;

@Entity
@Table(name = "beer_type_status")
public class BeerTypeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserBeerID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BeerID")
    private Beer beer;

    @ManyToOne
    @JoinColumn(name = "UserBeerTypeID")
    private UserBeerType beerType;

    public BeerTypeStatus() {

    }

    public BeerTypeStatus(User user, Beer beer, UserBeerType beerType) {
        this.user = user;
        this.beer = beer;
        this.beerType = beerType;
    }

    public int getId() {
        return id;
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

    public UserBeerType getBeerType() {
        return beerType;
    }

    public void setBeerType(UserBeerType beerType) {
        this.beerType = beerType;
    }
}

package com.beertag.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagID")
    private int id;


    @Column(name = "TagName")
    @Size(min = 2, max = 45, message = "Incorrect size for TagName")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
    @JsonIgnore
    private List<Beer> beers;



    public Tag() {
    }

    public Tag(String name){
        this.name = name;
        beers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(Beer beer) {
        beers.add(beer);
    }
}

package com.beertag.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "beer_styles")
public class BeerStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StyleID")
    private int id;

    @NotNull
    @Column(name = "StyleName")
    @Size(max = 15, message = "Incorrect size for StyleName")
    private String name;

    public BeerStyle() {}

    public BeerStyle(String name) {
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

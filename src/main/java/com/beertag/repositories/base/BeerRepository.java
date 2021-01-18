package com.beertag.repositories.base;

import com.beertag.models.Beer;
import com.beertag.models.Rating;
import com.beertag.models.Tag;

import java.util.List;

public interface BeerRepository {
    void add(Beer beer);

    void addTagToBeer(Beer beer, Tag tag,boolean flag);

    Beer getById(int id);

    void addBeer(Beer beer);

    void updateBeer(Beer beer);

    List<Beer> getAll();

    List<Beer> sortByABV();

    List<Beer> sortByBeerName();

    List<Beer> sortByRating();

    List<Beer> filterByStyle(String beerStyle);

    List<Beer> filterByCountry(String beerCountry);

    List<Beer> filterByTag(String tagName);

    List<Rating> getBeerRating(int beerId);

    void deleteBeer(int beerId);
}
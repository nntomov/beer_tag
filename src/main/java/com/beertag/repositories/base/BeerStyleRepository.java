package com.beertag.repositories.base;

import com.beertag.models.BeerStyle;

import java.util.List;

public interface BeerStyleRepository {

    List<BeerStyle> getAll();

    BeerStyle getById(int id);

    BeerStyle getByName(String style);
}

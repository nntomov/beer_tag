package com.beertag.repositories.base;

import com.beertag.models.OriginCountry;

import java.util.List;

public interface CountryRepository {

    List<OriginCountry> getAll();

    OriginCountry getById(int id);

    OriginCountry getByName(String country);
}

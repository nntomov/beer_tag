package com.beertag.services.base;

import com.beertag.models.OriginCountry;

import java.util.List;

public interface CountryService {

    List<OriginCountry> getAll();
}

package com.beertag.services.impl;

import com.beertag.models.OriginCountry;
import com.beertag.repositories.base.CountryRepository;
import com.beertag.services.base.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<OriginCountry> getAll() {
        return countryRepository.getAll();
    }
}


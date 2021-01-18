package com.beertag.services.impl;

import com.beertag.models.BeerStyle;
import com.beertag.repositories.base.BeerStyleRepository;
import com.beertag.services.base.BeerStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerStyleServiceImpl implements BeerStyleService {

    private BeerStyleRepository beerStyleRepository;

    @Autowired
    public BeerStyleServiceImpl(BeerStyleRepository beerStyleRepository) {
        this.beerStyleRepository = beerStyleRepository;
    }

    @Override
    public List<BeerStyle> getAll() {
        return beerStyleRepository.getAll();
    }
}

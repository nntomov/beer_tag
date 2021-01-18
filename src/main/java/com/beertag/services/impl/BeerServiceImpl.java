package com.beertag.services.impl;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.helpers.BeerPictureConverter;
import com.beertag.helpers.BeerShowManager;
import com.beertag.models.*;
import com.beertag.models.dtos.BeerDTO;
import com.beertag.models.dtos.BeerPictureDisplayerDTO;
import com.beertag.repositories.base.*;
import com.beertag.services.base.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerServiceImpl implements BeerService {

    private BeerRepository repository;
    private UserRepository userRepository;
    private BeerStyleRepository styleRepository;
    private CountryRepository countryRepository;
    private TagRepository tagRepository;
    private BeerShowManager beerShowManager = new BeerShowManager();

    @Autowired
    public BeerServiceImpl(BeerRepository repository, UserRepository userRepository, BeerStyleRepository styleRepository,
                           CountryRepository countryRepository, TagRepository tagRepository) {

        this.userRepository = userRepository;
        this.repository = repository;
        this.styleRepository = styleRepository;
        this.countryRepository = countryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void add(Beer beer) {
        repository.add(beer);
    }

    @Override
    public void addFromDTO(BeerDTO beerDTO, String creator) {
        User user = userRepository.getByName(creator);
        OriginCountry originCountry = countryRepository.getById(Integer.parseInt(beerDTO.getOriginCountry()));
        BeerStyle beerStyle = styleRepository.getById(Integer.parseInt(beerDTO.getStyle()));
        Beer newBeer = new Beer();
        newBeer.setName(beerDTO.getName());
        newBeer.setBreweryProducer(beerDTO.getBreweryProducer());
        newBeer.setABV(beerDTO.getABV());
        try {
            newBeer.setPicture(beerDTO.getPicture().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        newBeer.setDescription(beerDTO.getDescription());
        newBeer.setAddedBy(user);
        newBeer.setOriginCountry(originCountry);
        newBeer.setStyle(beerStyle);
        repository.addBeer(newBeer);
    }

    @Override
    public void editBeer(Beer newBeer, int beerToEdit) {
        Beer beer;

        try {
            beer = repository.getById(beerToEdit);
        } catch (DatabaseItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        beer.setName(newBeer.getName());
        beer.setBreweryProducer(newBeer.getBreweryProducer());
        beer.setOriginCountry(newBeer.getOriginCountry());
        beer.setStyle(newBeer.getStyle());
        beer.setABV(newBeer.getABV());
        beer.setDescription(newBeer.getDescription());
        beer.setPicture(newBeer.getPicture());
        repository.updateBeer(beer);
    }

    @Override
    public void remove(int beerID) {
        repository.deleteBeer(beerID);
    }

    @Override
    public List<Beer> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Beer> sortByABV() {
        return repository.sortByABV();
    }

    @Override
    public List<Beer> sortByBeerName() {
        return repository.sortByBeerName();
    }

    @Override
    public List<Beer> sortByRating() {
        return repository.sortByRating();
    }

    @Override
    public List<Beer> filterByCountry(String originCountry) {
        return repository.filterByCountry(originCountry);
    }

    @Override
    public List<Beer> filterByStyle(String beerStyle) {
        return repository.filterByStyle(beerStyle);
    }

    @Override
    public List<Beer> filterByTag(String tagName) {
        return repository.filterByTag(tagName);
    }

    @Override
    public void setShowOptions(BeerShowManager newShowManager) {
        beerShowManager = newShowManager;
    }

    @Override
    public Page<BeerPictureDisplayerDTO> findPaginated(Pageable pageable) {
        List<Beer> beers = getSpecifiedSortBeers(getSpecifiedFilterBeers());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<BeerPictureDisplayerDTO> list;

        if (beers.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, beers.size());
            list = beers.subList(startItem, toIndex)
                    .stream()
                    .map(BeerPictureConverter::getBeer)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), beers.size());
    }

    private List<Beer> getSpecifiedFilterBeers() {
        if (beerShowManager.getFilterBy().equalsIgnoreCase("style")) {
            return repository.filterByStyle(beerShowManager.getFilterValue());
        } else if (beerShowManager.getFilterBy().equalsIgnoreCase("country")) {
            return repository.filterByStyle(beerShowManager.getFilterValue());
        } else if (beerShowManager.getFilterBy().equalsIgnoreCase("tag")) {
            return repository.filterByStyle(beerShowManager.getFilterValue());
        } else {
            return null;
        }
    }

    private List<Beer> getSpecifiedSortBeers(List<Beer> filteredBeers) {
        if (filteredBeers == null) {
            if (beerShowManager.getOrderBy().equalsIgnoreCase("abv")) {
                return repository.sortByABV();
            } else if (beerShowManager.getOrderBy().equalsIgnoreCase("name")) {
                return repository.sortByBeerName();
            } else if (beerShowManager.getOrderBy().equalsIgnoreCase("rating")) {
                return repository.sortByRating();
            } else {
                return repository.getAll();
            }
        }

        if (beerShowManager.getOrderBy().equalsIgnoreCase("abv")) {
            return filteredBeers.stream().sorted(Comparator.comparing(Beer::getABV).reversed()).collect(Collectors.toList());
        } else if (beerShowManager.getOrderBy().equalsIgnoreCase("name")) {
            return filteredBeers.stream().sorted(Comparator.comparing(Beer::getName)).collect(Collectors.toList());
        } else if (beerShowManager.getOrderBy().equalsIgnoreCase("rating")) {
            filteredBeers.forEach(beer -> beer.setAvgRating(calculateAvgRating(beer.getId())));
            return filteredBeers.stream()
                    .sorted(Comparator.comparing(Beer::getAvgRating).reversed()).collect(Collectors.toList());
        } else {
            return filteredBeers;
        }
    }

    @Override
    public Beer getById(int id) {
        return repository.getById(id);
    }

    @Override
    public double calculateAvgRating(int beerId) {
        double avgRating = 0;
        List<Rating> avgList = repository.getBeerRating(beerId);
        if (avgList.isEmpty()) {
            return avgRating;
        }

        avgRating = avgList.stream().map(Rating::getRating).reduce(0, Integer::sum);
        return avgRating / avgList.size();
    }

    @Override
    public void addTagToBeer(int beerID, String name) {
        boolean unknownTag = false;
        Tag tag = tagRepository.getByName(name);

        if (tag == null) {
            tag = new Tag(name);
            unknownTag = true;
        }

        Beer beer = repository.getById(beerID);
        repository.addTagToBeer(beer, tag, unknownTag);
    }
}

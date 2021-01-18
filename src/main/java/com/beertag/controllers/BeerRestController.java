package com.beertag.controllers;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.Beer;
import com.beertag.services.base.BeerService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/beer")
public class BeerRestController {

    private BeerService service;

    @Autowired
    BeerRestController(BeerService service) {
        this.service = service;
    }


    @GetMapping
    public List<Beer> getAll() {
        return service.getAll();
    }

    @RequestMapping(params = {"orderBy"}, method = RequestMethod.GET)
    public List<Beer> orderBeers(String orderBy) {
        try {
            if (orderBy.equalsIgnoreCase("abv")) {
                return service.sortByABV();
            } else if (orderBy.equalsIgnoreCase("name")) {
                return service.sortByBeerName();
            } else if (orderBy.equalsIgnoreCase("rating")) {
                return service.sortByRating();
            } else {
                return service.getAll();
            }
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(params = {"filterBy", "val"}, method = RequestMethod.GET)
    public List<Beer> filterBeers(String filterBy, String val) {
        try {
            if (filterBy.equalsIgnoreCase("style")) {
                return service.filterByStyle(val);
            } else if (filterBy.equalsIgnoreCase("country")) {
                return service.filterByCountry(val);
            } else if (filterBy.equalsIgnoreCase("tag")) {
                return service.filterByTag(val);
            } else {
                return service.getAll();
            }
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/edit", params = {"id"})
    public void editBeer(@Valid @RequestBody Beer beer, int id) {
        try {
            service.editBeer(beer, id);
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }


    @PostMapping("/new")
    public void addBeer(@Valid @RequestBody Beer beer) {
        try {
            service.add(beer);
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to access database.");
        }
    }

    @RequestMapping(value = "/delete", params = {"beerId"}, method = RequestMethod.DELETE)
    public void removeBeer(int beerId) {
        try {
            service.remove(beerId);
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/tag", params = {"beerID", "name"}, method = RequestMethod.POST)
    public void addTagToBeer(int beerID, String name) {
        try {
            service.addTagToBeer(beerID, name);
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to access database.");
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

}

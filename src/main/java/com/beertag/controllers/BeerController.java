package com.beertag.controllers;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.Beer;
import com.beertag.services.base.BeerService;
import com.beertag.services.base.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/beer")
public class BeerController {

    private BeerService beerService;
    private UserService userService;

    @Autowired
    public BeerController(BeerService beerService, UserService userService) {
        this.beerService = beerService;
        this.userService = userService;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getBeerInfo(Model model, @RequestParam("id") int id) {
        try {
            Beer beer = beerService.getById(id);
            model.addAttribute("beerName", beer.getName());
            model.addAttribute("beerProducer", beer.getBreweryProducer());
            model.addAttribute("beerCountry", beer.getOriginCountry().getName());
            model.addAttribute("beerDesc", beer.getDescription());
            model.addAttribute("beerABV", beer.getABV());
            model.addAttribute("beerStyle", beer.getStyle().getName());
            model.addAttribute("beerPicture", new String(Base64.encodeBase64(beer.getPicture())));
            model.addAttribute("beerTags", beer.getTags());
            model.addAttribute("avgRating", beerService.calculateAvgRating(beer.getId()));
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }

        return "show_beer";
    }

    @RequestMapping(value = "/of-user-info", method = RequestMethod.GET)
    public String getUserBeerInfo(Model model, @RequestParam("id") int id) {

        boolean isItMine = false;
        try {
            Beer beer = beerService.getById(id);
            int userId = userService.getByName(SecurityContextHolder.getContext()
                    .getAuthentication().getName()).getId();

            if (beer.getAddedBy().getId() == userId) {
                isItMine = true;
            }

            model.addAttribute("userId", userId);
            model.addAttribute("beerId", id);
            model.addAttribute("beerName", beer.getName());
            model.addAttribute("beerDesc", beer.getDescription());
            model.addAttribute("beerProducer", beer.getBreweryProducer());
            model.addAttribute("beerPicture", new String(Base64.encodeBase64(beer.getPicture())));
            model.addAttribute("beerCountry", beer.getOriginCountry().getName());
            model.addAttribute("beerABV", beer.getABV());
            model.addAttribute("userID", beer.getAddedBy().getId());
            model.addAttribute("creatorName", beer.getAddedBy().getUsername());
            model.addAttribute("beerStyle", beer.getStyle().getName());
            model.addAttribute("userRating", userService.getBeerRating(userId, id));
            model.addAttribute("avgRating", beerService.calculateAvgRating(beer.getId()));
            model.addAttribute("beerTags", beer.getTags());
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
        if (isItMine) {
            return "show_my_beer";
        } else {
            return "show_user_beer";
        }
    }
}

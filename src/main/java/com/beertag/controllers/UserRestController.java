package com.beertag.controllers;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.Beer;
import com.beertag.models.User;
import com.beertag.services.base.UserService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private UserService service;

    @Autowired
    public UserRestController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/most-ranked", params = {"userId"})
    public List<Beer> getMostRankedBeers(int userId) {
        return service.getMostRankedBeers(userId);
    }

    @GetMapping("/{name}")
    public User getByName(@PathVariable String name) {
        return service.getByName(name);
    }

//    @PutMapping(value = "/edit", params = {"id"})
//    public void editBeer(@Valid @RequestBody User user, int id) {
//        try {
//            service.editUser(user, id);
//        } catch (DatabaseItemNotFoundException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    e.getMessage());
//        } catch (HibernateException he) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    he.getMessage());
//        }
//    }


    @RequestMapping(value = "/rate", params = {"userID", "beerID", "rating"}, method = RequestMethod.POST)
    public void rateBeer(int userID, int beerID, int rating) {
        try {
            service.rateBeer(userID, beerID, rating);
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(value = "/set-type", params = {"beerID", "userID", "type"}, method = RequestMethod.POST)
    public void changeStatus(int userID, int beerID, String type) {
        try {
            service.setBeerType(beerID, userID, type);
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to access database.");
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(value = "/delete", params = {"userId"}, method = RequestMethod.DELETE)
    public void deleteUser(int userId) {
        try {
            service.deleteUser(userId);
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    he.getMessage());
        }
    }
}

package com.beertag.repositories.base;

import com.beertag.models.Beer;
import com.beertag.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getByName(String username);

    boolean isPresent(String username);

    void updateUser(User user);

    void create(User user);

    void rateBeer(User user, Beer beer, int rating);

    User getById(int id);

    List<Beer> getMostRankedBeersOfUser(int userId);

    List<Beer> getWishedBeers(int userId);

    List<Beer> getDrankBeers(int userId);

    List<Beer> getUsersBeers(int userId);

    void setBeerType(Beer beer, User user, String type);

    void deleteUser(int userId, BeerRepository beerRepo);

    int getBeerRating(int userId, int beerId);
}
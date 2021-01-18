package com.beertag.services.base;

import com.beertag.models.Beer;
import com.beertag.models.User;
import com.beertag.models.dtos.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();

    User getByName(String username);

    User getByID(int id);

    void deleteUser(int userId);

    void create(UserDTO user);

    void editUser(UserDTO newUser, int userToEdit);

    void rateBeer(int userID, int beerID, int rating);

    List<Beer> getMostRankedBeers(int userId);

    List<Beer> getWishedBeers(int userId);

    List<Beer> getDrankBeers(int userId);

    List<Beer> getUsersBeers(int userId);

    void setBeerType(int beerId, int userId, String type);

    int getBeerRating(int userId, int beerId);
}

package com.beertag.services.impl;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.Beer;
import com.beertag.models.User;
import com.beertag.models.dtos.UserDTO;
import com.beertag.repositories.base.AuthorityRepository;
import com.beertag.repositories.base.BeerRepository;
import com.beertag.repositories.base.UserRepository;
import com.beertag.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BeerRepository beerRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeerRepository beerRepository,
                           PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.beerRepository = beerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getByName(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId, beerRepository);
    }

    @Override
    public void create(UserDTO user) {
        User newUser = new User();

        changeUserData(newUser, user);
        userRepository.create(newUser);
    }

    @Override
    public void editUser(UserDTO newUser, int userToEdit) {
        User user;

        try {
            user = userRepository.getById(userToEdit);
            changeUserData(user, newUser);
        } catch (DatabaseItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        userRepository.updateUser(user);
    }

    private void changeUserData(User changeTo, UserDTO changeFrom) {
        String encodedPassword = passwordEncoder.encode(changeFrom.getPassword());
        changeTo.setUsername(changeFrom.getUsername());
        changeTo.setPassword(encodedPassword);

        try {
            changeTo.setPhoto(changeFrom.getAvatar().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rateBeer(int userID, int beerID, int rating) {
        try {
            User user = userRepository.getById(userID);
            Beer beer = beerRepository.getById(beerID);
            userRepository.rateBeer(user, beer, rating);
        } catch (DatabaseItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Beer> getMostRankedBeers(int userId) {
        return userRepository.getMostRankedBeersOfUser(userId);
    }

    @Override
    public List<Beer> getWishedBeers(int userId) {
        return userRepository.getWishedBeers(userId);
    }

    @Override
    public List<Beer> getDrankBeers(int userId) {
        return userRepository.getDrankBeers(userId);
    }

    @Override
    public List<Beer> getUsersBeers(int userId) {
        return userRepository.getUsersBeers(userId);
    }

    @Override
    public void setBeerType(int beerId, int userId, String type) {
        try {
            Beer beer = beerRepository.getById(beerId);
            User user = userRepository.getById(userId);
            userRepository.setBeerType(beer, user, type);
        } catch (DatabaseItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int getBeerRating(int userId, int beerId) {
        return userRepository.getBeerRating(userId, beerId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByName(username);
        System.out.println(passwordEncoder.encode(user.getPassword()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public User getByID(int id) {
        return userRepository.getById(id);
    }
}

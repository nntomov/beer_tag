package com.beertag.controllers;

import com.beertag.helpers.BeerPictureConverter;
import com.beertag.models.dtos.BeerPictureDisplayerDTO;
import com.beertag.models.dtos.UserDTO;
import com.beertag.services.base.BeerService;
import com.beertag.services.base.UserService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("")
public class LoginController {

    private UserService userService;
    private BeerService beerService;

    @Autowired
    public LoginController(UserService userService, BeerService beerService) {
        this.userService = userService;
        this.beerService = beerService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<BeerPictureDisplayerDTO> displayerDTOS = beerService.getAll()
                .stream()
                .map(BeerPictureConverter::getBeer)
                .collect(Collectors.toList());
        if (displayerDTOS.isEmpty()) {
            model.addAttribute("firstBeer", new BeerPictureDisplayerDTO());
        } else {
            model.addAttribute("firstBeer", displayerDTOS.get(0));
            displayerDTOS.remove(0);
        }

        model.addAttribute("allBeers", displayerDTOS);
        model.addAttribute("user", new UserDTO());

        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserDTO user) {
        try {
            userService.create(user);
        } catch (HibernateException he) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, he.getMessage());
        }
        return "redirect:/";
    }
}


package com.beertag.controllers;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.helpers.BeerPictureConverter;
import com.beertag.helpers.BeerShowManager;
import com.beertag.helpers.UserPictureConverter;
import com.beertag.models.*;
import com.beertag.models.dtos.BeerDTO;
import com.beertag.models.dtos.BeerPictureDisplayerDTO;
import com.beertag.models.dtos.EditUserDTO;
import com.beertag.models.dtos.UserPictureDisplayerDTO;
import com.beertag.services.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/home")
public class UserHomeController {

    private BeerService beerService;
    private BeerStyleService styleService;
    private CountryService countryService;
    private UserService userService;
    private AuthorityService authorityService;

    @Autowired
    public UserHomeController(BeerService beerService, BeerStyleService styleService, CountryService countryService, UserService userService, AuthorityService authorityService) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.countryService = countryService;
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @RequestMapping(value = "/show-options", method = RequestMethod.POST)
    public String setShowOptions(@ModelAttribute BeerShowManager newShowManager){
        beerService.setShowOptions(newShowManager);
        return "redirect:/home/all_beers";
    }

    @RequestMapping(value = "/all_beers", method = RequestMethod.GET)
    public String listBeers(Model model, @RequestParam("page") Optional<Integer> page) {

        int currentPage = page.orElse(1);
        final int pageSize = 6;

        Page<BeerPictureDisplayerDTO> beerPage = beerService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("beerPage", beerPage);

        int totalPages = beerPage.getTotalPages();
        if (currentPage > totalPages) {
            totalPages--;
        }
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("showManager", new BeerShowManager());
        return "all_beers";
    }

    @RequestMapping(value = "/all_users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        List<User> users = userService.getAll();
        List<UserPictureDisplayerDTO> result = users
                .stream()
                .filter(user -> !user.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
                .map(UserPictureConverter::getUser)
                .collect(Collectors.toList());
        model.addAttribute("users", result);
        Authority authority = authorityService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (authority.getAuthority().equals("ADMIN")) {
            return "all_users_adminPage";
        } else {
            return "all_users_userPage";
        }
    }

    @GetMapping("/newBeer")
    public String createNewBeer(Model model) {
        List<BeerStyle> allStyles = styleService.getAll();
        List<OriginCountry> allCountries = countryService.getAll();
        model.addAttribute("stylesList", allStyles);
        model.addAttribute("countryList", allCountries);
        model.addAttribute("beerDTO", new BeerDTO());
        return "add_beer";
    }

    @PostMapping("/newBeer")
    public String addBeer(@Valid @ModelAttribute BeerDTO beerDTO) {
        beerService.addFromDTO(beerDTO, SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/home/all_beers";
    }

    @RequestMapping(value = "/my_account", method = RequestMethod.GET)
    public String getUserInfo(Model model) {
        try {
            UserPictureDisplayerDTO user =
                    UserPictureConverter.getUser(userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName()));

            addingAttributesToModel(model, user);
            List<BeerPictureDisplayerDTO> myBeers = userService.getUsersBeers(user.getId())
                    .stream()
                    .map(BeerPictureConverter::getBeer)
                    .collect(Collectors.toList());
            if (myBeers.isEmpty()) {
                model.addAttribute("firstBeer", new BeerPictureDisplayerDTO());
            } else {
                model.addAttribute("firstBeer", myBeers.get(0));
                myBeers.remove(0);
            }
            model.addAttribute("myBeers", myBeers);
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }

        return "my_account";
    }

    @RequestMapping(value = "/user_acount", method = RequestMethod.GET)
    public String getUserInfo(Model model, @RequestParam("id") int id) {
        try {
            UserPictureDisplayerDTO user = UserPictureConverter.getUser(userService.getByID(id));

            addingAttributesToModel(model, user);
        } catch (DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }

        return "user_info";
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public String rateBeer(@RequestParam(name = "beerID") Integer beerID,
                           @RequestParam Map<String, String> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            int vote = Integer.parseInt(body.get("star"));
            User user = userService.getByName(auth.getName());
            Beer beer = beerService.getById(beerID);
            userService.rateBeer(user.getId(), beer.getId(), vote);
        }
        return "redirect:/beer/of-user-info?id=" + beerID;
    }


    @RequestMapping(value = "/edit", params = {"id"}, method = RequestMethod.PUT)
    public String addBeer(@Valid @ModelAttribute EditUserDTO editUser, int id) {
        try {
            Authority auth = authorityService.getByUsername(userService.getByID(id).getUsername());
            auth.setAuthority(editUser.getAuthority());
            auth.setUsername(editUser.getUsername());
            authorityService.updateAuthority(auth);
            userService.editUser(editUser, id);
        } catch (
                DatabaseItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }

        return "redirect:/home/all_users";
    }

    @GetMapping(value = "/edit", params = {"id"})
    public String editUser(Model model, int id) {
        EditUserDTO user = new EditUserDTO();
        model.addAttribute("id", id);
        model.addAttribute("user", user);

        return "edit_user";
    }

    private void addingAttributesToModel(Model model, UserPictureDisplayerDTO user) {
        List<BeerPictureDisplayerDTO> topBeers = userService.getMostRankedBeers(user.getId())
                .stream()
                .map(BeerPictureConverter::getBeer)
                .collect(Collectors.toList());

        List<BeerPictureDisplayerDTO> wishList = userService.getWishedBeers(user.getId())
                .stream()
                .map(BeerPictureConverter::getBeer)
                .collect(Collectors.toList());

        if (wishList.isEmpty()) {
            model.addAttribute("firstWished", new BeerPictureDisplayerDTO());
        } else {
            model.addAttribute("firstWished", wishList.get(0));
            wishList.remove(0);
        }

        List<BeerPictureDisplayerDTO> drankList = userService.getDrankBeers(user.getId())
                .stream()
                .map(BeerPictureConverter::getBeer)
                .collect(Collectors.toList());

        if (drankList.isEmpty()) {
            model.addAttribute("firstDrank", new BeerPictureDisplayerDTO());
        } else {
            model.addAttribute("firstDrank", drankList.get(0));
            drankList.remove(0);
        }

        model.addAttribute("topBeers", topBeers);
        model.addAttribute("wishedBeers", wishList);
        model.addAttribute("drankBeers", drankList);
        model.addAttribute("user", user);
    }
}



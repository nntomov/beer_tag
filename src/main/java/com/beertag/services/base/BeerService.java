package com.beertag.services.base;

import com.beertag.helpers.BeerShowManager;
import com.beertag.models.Beer;
import com.beertag.models.dtos.BeerDTO;
import com.beertag.models.dtos.BeerPictureDisplayerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BeerService {
    void add(Beer beer);

    void addFromDTO(BeerDTO beerDTO, String creator);

    void editBeer(Beer newBeer, int beerToEdit);

    void remove(int beerID);

    List<Beer> getAll();

    void addTagToBeer(int beerID, String name);

    List<Beer> sortByABV();

    List<Beer> sortByBeerName();

    List<Beer> sortByRating();

    List<Beer> filterByCountry(String originCountry);

    List<Beer> filterByStyle(String beerStyle);

    List<Beer> filterByTag(String tagName);

    Page<BeerPictureDisplayerDTO> findPaginated(Pageable pageable);

    void setShowOptions(BeerShowManager newShowManager);

    Beer getById(int id);

    double calculateAvgRating(int beerId);
}
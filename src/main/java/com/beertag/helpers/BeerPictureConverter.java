package com.beertag.helpers;

import com.beertag.models.Beer;
import com.beertag.models.dtos.BeerPictureDisplayerDTO;
import org.apache.tomcat.util.codec.binary.Base64;

public class BeerPictureConverter {

    public static BeerPictureDisplayerDTO getBeer(Beer beer) {
        BeerPictureDisplayerDTO result = new BeerPictureDisplayerDTO();

        result.setId(beer.getId());
        result.setPicture(new String(Base64.encodeBase64(beer.getPicture())));
        return result;
    }
}

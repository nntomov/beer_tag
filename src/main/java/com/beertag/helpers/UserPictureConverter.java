package com.beertag.helpers;

import com.beertag.models.User;
import com.beertag.models.dtos.UserPictureDisplayerDTO;
import org.apache.tomcat.util.codec.binary.Base64;

public class UserPictureConverter {
    public static UserPictureDisplayerDTO getUser(User user) {
        UserPictureDisplayerDTO result = new UserPictureDisplayerDTO();

        result.setId(user.getId());
        result.setName(user.getUsername());
        result.setPicture(new String(Base64.encodeBase64(user.getPhoto())));
        return result;
    }
}


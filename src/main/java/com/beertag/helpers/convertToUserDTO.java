//package com.telerikacademy.beertag.helpers;
//
//import com.telerikacademy.beertag.models.LoginUserInfo;
//import com.telerikacademy.beertag.models.User;
//import com.telerikacademy.beertag.models.dtos.UserDTO;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Base64;
//
//public class  convertToUserDTO {
//    public UserDTO getUserDTO(User user){
//        UserDTO result = new UserDTO();
//        result.setUsername(user.getUsername());
//        result.setPassword(user.getPassword());
//
//        try {
//            result.setAvatar(new String(user.getPhoto(), "UTF-8"));
//        } catch (UnsupportedEncodingException u){
//            System.out.println(u.getMessage());
//        }
//        return result;
//    }
//
//}

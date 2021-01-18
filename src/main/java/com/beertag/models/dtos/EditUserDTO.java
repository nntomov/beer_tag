package com.beertag.models.dtos;

public class EditUserDTO extends UserDTO {

    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}

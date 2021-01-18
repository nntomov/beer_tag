package com.beertag.services.base;

import com.beertag.models.Authority;

import java.util.List;

public interface AuthorityService {

    List<Authority> getAll();

    Authority getById(int id);

    List<Authority> getByAuthority(String authoritory);

    Authority getByUsername(String username);

    void updateAuthority(Authority authority);
}

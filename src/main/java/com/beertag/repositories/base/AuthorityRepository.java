package com.beertag.repositories.base;

import com.beertag.models.Authority;

import java.util.List;

public interface AuthorityRepository {

    List<Authority> getAll();

    Authority getById(int id);

    List<Authority> getByAuthority(String authority);

    Authority getByUsername(String username);

    void updateAuthority(Authority authority);
}
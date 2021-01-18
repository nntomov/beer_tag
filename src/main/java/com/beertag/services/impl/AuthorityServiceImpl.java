package com.beertag.services.impl;

import com.beertag.models.Authority;
import com.beertag.repositories.base.AuthorityRepository;
import com.beertag.services.base.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<Authority> getAll() {
        return authorityRepository.getAll();
    }

    @Override
    public Authority getById(int id) {
        return authorityRepository.getById(id);
    }

    @Override
    public List<Authority> getByAuthority(String authoritory) {
        return authorityRepository.getByAuthority(authoritory);
    }

    @Override
    public Authority getByUsername(String username) {
        return authorityRepository.getByUsername(username);
    }

    @Override
    public void updateAuthority(Authority authority) {
        authorityRepository.updateAuthority(authority);
    }
}

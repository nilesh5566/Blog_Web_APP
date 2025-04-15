package org.nilesh.blogproject.services;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.nilesh.blogproject.models.Authority;
import org.nilesh.blogproject.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    
    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    public Optional<Authority> findByID(Long id){
        return  authorityRepository.findById(id);
    }
}
package org.nilesh.blogproject.services;

import java.util.Optional;

import org.nilesh.blogproject.models.Account;
import org.nilesh.blogproject.models.Authority;
import org.nilesh.blogproject.repositories.AccountRepository;
import org.nilesh.blogproject.utils.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
       account.setRole(Roles.USER.getRole());
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole()));

         
      for(Authority auth:account.getAuthorities()){
        authorities.add(new SimpleGrantedAuthority(auth.getName()));

      }


        return new User(account.getEmail(), account.getPassword(), authorities);
    }

    public Optional<Account> findByEmailIgnoreCase(String email) {
   return accountRepository.findByEmailIgnoreCase(email);
    }

    public Optional<Account> fingById(Long id) {
        return accountRepository.findById(id);
    }

   
}

package org.nilesh.blogproject.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nilesh.blogproject.models.Account;
import org.nilesh.blogproject.models.Authority;
import org.nilesh.blogproject.models.Post;
import org.nilesh.blogproject.services.AccountService;
import org.nilesh.blogproject.services.AuthorityService;
import org.nilesh.blogproject.services.PostService;
import org.nilesh.blogproject.utils.constants.Privillages;
import org.nilesh.blogproject.utils.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component  
public class SeedData implements CommandLineRunner {

    @Autowired 
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {
        
        // Seed authorities
        for (Privillages auth : Privillages.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getAuthorityString());
            authorityService.save(authority);
        }

        // Create accounts
        Account account01 = createAccount("account01@gmail.com", "password01", "User01", "Aman", Roles.EDITOR.getRole());
        Account account02 = createAccount("account02@gmail.com", "password02", "User02", "Nilesh", Roles.EDITOR.getRole());
        Account account03 = createAccount("account03@gmail.com", "password03", "User03", "Nitin", Roles.ADMIN.getRole());
        Account account04 = createAccount("account04@gmail.com", "password04", "User04", "Naman", Roles.ADMIN.getRole());

        // Set authorities
        Set<Authority> authorities = new HashSet<>();
        authorityService.findByID(Privillages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        authorityService.findByID(Privillages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);

        // Save accounts
        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04); 

        // Create posts if empty
        List<Post> posts = postService.getAll();
        if (posts.isEmpty()) {
            createPost("Post 01", "Post 01 Body............", account01);
            createPost("Post 02", "Post 02 Body............", account02);
            createPost("Post 03", "Post 03 Body............", account03);
            createPost("Post 04", "Post 04 Body............", account04);
        }
    }

    private Account createAccount(String email, String password, String firstname, String lastname, String role) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setRole(role);
        return account;
    }

    private void createPost(String title, String body, Account account) {
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setAccount(account);
        postService.save(post);
    }
}
package org.nilesh.blogproject.Controller;

import java.security.Principal;
import java.util.Optional;

import org.nilesh.blogproject.models.Account;
import org.nilesh.blogproject.models.Post;
import org.nilesh.blogproject.services.AccountService;
import org.nilesh.blogproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            
            if(principal != null && principal.getName().equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }
            return "post";
        }
        return "404";
    }
     
    @GetMapping("/add_post")
    @PreAuthorize("isAuthenticated()")
    public String showAddPostForm(Model model, Principal principal) {
        Account account = accountService.findByEmailIgnoreCase(principal.getName())
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        Post post = new Post();
        post.setAccount(account);
        model.addAttribute("post", post);
        return "add_post";
    }
    
    @PostMapping("/add_post")
    @PreAuthorize("isAuthenticated()")
    public String addPost(@Valid @ModelAttribute Post post, 
                        BindingResult result,
                        Principal principal,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "add_post";
        }
        
        // Verify ownership
        if (!principal.getName().equals(post.getAccount().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "You can only create posts for your own account");
            return "redirect:/post";
        }
        
        postService.save(post);
        redirectAttributes.addFlashAttribute("success", "Post created successfully!");
        return "redirect:/post/" + post.getId();
    }
    
    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.getById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
            
        // Verify ownership
        if (!principal.getName().equals(post.getAccount().getEmail())) {
            return "redirect:/post/" + id;
        }
        
        model.addAttribute("post", post);
        return "post_edit";
    }

    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String  updatePost(@PathVariable Long id, @ModelAttribute Post post ){
        Optional<Post> optionalPost = postService.getById(id);
        if(optionalPost.isPresent()){
            Post exiPost=optionalPost.get();
            exiPost.setTitle(post.getTitle());
            exiPost.setBody(post.getBody());
          postService.save(exiPost);
        }
        return "redirect:/post/"+post.getId();
    }

    @GetMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id){
        Optional<Post> optionalPost=postService.getById(id);
        if(optionalPost.isPresent()){
            Post post=optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        }else{
           return "redirect:/?error"; 
        }
    }

}
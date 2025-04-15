package org.nilesh.blogproject.Controller;

import java.util.List;

import org.nilesh.blogproject.models.Post;
import org.nilesh.blogproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private PostService postService;
     @GetMapping("/home")
    public String getMethodName(@RequestParam(required = false, defaultValue = "default") String param, Model model) {
        model.addAttribute("param", param);
        return "home";  // ✅ This should return the view name, not an empty string
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Post>posts=postService.getAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    
    @GetMapping("/editor")
    public String editor(Model model) {
          return "editor";
    }
}
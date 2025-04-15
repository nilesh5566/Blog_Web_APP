package org.nilesh.blogproject.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.nilesh.blogproject.models.Post;
import org.nilesh.blogproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//for intraction
@Service
public class PostService {
    
@Autowired
private PostRepository postRepository;

public Optional<Post> getById(Long id){
return  postRepository.findById(id);
}

public List<Post> getAll(){
return  postRepository.findAll();
}
public void delete(Post post){
 postRepository.delete(post);
}

public Post save(Post post){
    if(post.getId()==null){
        post.setCreatedAt(LocalDateTime.now());
    }

    post.setUpdatedAt(LocalDateTime.now());
    return postRepository.save(post);
}
public Page<Post> getAll(Pageable pageable) {
    return postRepository.findAll(pageable);
}

}
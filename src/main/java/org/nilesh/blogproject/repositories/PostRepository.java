package org.nilesh.blogproject.repositories;
import org.nilesh.blogproject.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
//for intraction Db
import org.springframework.stereotype.Repository;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

} 
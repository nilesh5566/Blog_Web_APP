package org.nilesh.blogproject.repositories;

import org.nilesh.blogproject.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    
}
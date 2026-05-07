package com.andrew.blog.repositories;

import com.andrew.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface PostRepository extends JpaRepository<Post, Long> {
}

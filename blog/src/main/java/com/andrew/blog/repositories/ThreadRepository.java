package com.andrew.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ThreadRepository extends JpaRepository<ThreadRepository, Long> {
}

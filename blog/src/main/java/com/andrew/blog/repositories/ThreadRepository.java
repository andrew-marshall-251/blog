package com.andrew.blog.repositories;

import com.andrew.blog.entities.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ThreadRepository extends JpaRepository<Thread, Long> {
}

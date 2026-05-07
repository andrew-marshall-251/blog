package com.andrew.blog.repositories;

import com.andrew.blog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface RoleRepository extends JpaRepository<Role, Long> {
}

package com.example.democafeclientsystem.repositories;

import com.example.democafeclientsystem.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}

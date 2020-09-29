package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepo extends CrudRepository<Link, Long> {
    Link findByNewName(String originalName);
}

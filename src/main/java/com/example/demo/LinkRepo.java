package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepo extends CrudRepository<Link, Long> {


    Optional<Link> findByNewName( String newName);

    @Modifying
    @Query("update Link l set l.counter = l.counter+1 where l.id = :id")
    void increase(@Param("id") Long id);
    List<Link> findAllByIp(String ip);
    List<Link> findAll();

    void deleteByDeleteKey(String deleteKey);

}

package com.example.demo.repository;

import com.example.demo.entity.LinkTracker;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkTrackerRepository extends PagingAndSortingRepository<LinkTracker, Long> {

    List<LinkTracker> findAll();

    Optional<LinkTracker> findById(Long id);

    LinkTracker findByCity(String city);

}

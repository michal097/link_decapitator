package com.example.demo.repository;

import com.example.demo.entity.Stats;
import org.springframework.data.repository.CrudRepository;

public interface LinkStatsRepo extends CrudRepository<Stats, Long> {
}

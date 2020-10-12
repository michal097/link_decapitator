package com.example.demo.repository;

import com.example.demo.entity.LinkTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkTrackerRepository extends PagingAndSortingRepository<LinkTracker, Long> {

   List<LinkTracker> findAll();
   Page<LinkTracker> findAll(Pageable pageable);

   @Modifying
   @Query("update LinkTracker lt set lt.countLinksByIp = lt.countLinksByIp+1 where lt.linkTrackerIp = :linkTrackerIp")
   void updateIpCounter(LinkTracker linkTracker);

}

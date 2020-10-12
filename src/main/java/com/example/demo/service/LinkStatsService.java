package com.example.demo.service;

import com.example.demo.entity.LinkTracker;
import com.example.demo.repository.LinkTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LinkStatsService {


    private final LinkTrackerRepository linkTrackerRepository;

    @Autowired
    LinkStatsService(LinkTrackerRepository linkTrackerRepository) {

        this.linkTrackerRepository = linkTrackerRepository;
    }

    public long countAllLinks() {

        return linkTrackerRepository.findAll()
                .stream()
                .map(LinkTracker::getCountLinksByIp)
                .reduce(Integer::sum)
                .orElse(0);
    }

}

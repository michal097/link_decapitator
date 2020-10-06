package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LinkStatsService {

    private final LinkRepo linkRepo;

    @Autowired
    LinkStatsService(LinkRepo linkRepo){
        this.linkRepo = linkRepo;
    }

    public long countAllRedirectedURLs(){
        return linkRepo.findAll()
                .stream()
                .map(Link::getCounter)
                .reduce(Integer::sum)
                .orElse(0);

    }

    public long countAllLinks(){
        return linkRepo.findAll().size();
    }

}

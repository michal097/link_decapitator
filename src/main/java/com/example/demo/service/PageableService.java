package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.entity.LinkTracker;
import com.example.demo.repository.LinkRepo;
import com.example.demo.repository.LinkTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageableService {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;
    private final LinkTrackerRepository linkTrackerRepository;


    @Autowired
    PageableService(LinkRepo linkRepo,
                    LinkValidatorService linkValidatorService,
                    LinkTrackerRepository linkTrackerRepository) {
        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
        this.linkTrackerRepository = linkTrackerRepository;

    }

    public List<Link> getAllLinksWithPagination(Integer pageNumber, Integer pageSize, String sort) {


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
        Page<Link> page = linkRepo.findAllByIp(linkValidatorService.getActualIP(), pageable);

        if (page.hasContent()) {
            return page.getContent();
        } else return new ArrayList<>();
    }

    public List<LinkTracker> linkTrackerList(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
        Page<LinkTracker> myPage = linkTrackerRepository.findAll(pageable);

        if (myPage.hasContent()) {
            return myPage.getContent();
        } else
            return new ArrayList<>();
    }
}

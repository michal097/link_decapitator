package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
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

    @Autowired
    PageableService(LinkRepo linkRepo, LinkValidatorService linkValidatorService){
        this.linkRepo=linkRepo;
        this.linkValidatorService=linkValidatorService;
    }

    public List<Link> getAllLinksWithPagination(Integer pageNumber, Integer pageSize, String sort){

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
        Page<Link> page = linkRepo.findAllByIp(linkValidatorService.getActualIP(), pageable);

        if(page.hasContent()){
            return page.getContent();
        }
        else return new ArrayList<>();
    }
}

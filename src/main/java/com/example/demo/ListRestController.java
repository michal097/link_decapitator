package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class ListRestController {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;

    @Autowired
    public ListRestController(LinkRepo linkRepo, LinkValidatorService linkValidatorService) {
        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
    }

    @GetMapping("allUrls")
    public List<Link> allUrls(){

        return linkRepo.findAllByIp(linkValidatorService.getActualIP());
    }

    @DeleteMapping("delete/{deleteKey}")
    @Transactional
    public ResponseEntity<Link> deleteLink(@PathVariable("deleteKey")String deleteKey){

        boolean deleteKeyIsPresent = linkRepo.findAll()
                .stream()
                .filter(ip->ip.getIp().equals(linkValidatorService.getActualIP()))
                .anyMatch(k->k.getDeleteKey().equals(deleteKey));

        if(deleteKeyIsPresent) {
            linkRepo.deleteByDeleteKey(deleteKey);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}

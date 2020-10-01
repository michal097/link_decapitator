package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@RestController
public class ListRestController {

    private final LinkRepo linkRepo;

    @Autowired
    public ListRestController(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    @GetMapping("counter/{newName}")
    public Integer visitCounter(@PathVariable String newName){
        Optional<Link> getLink = linkRepo.findByNewName(newName);
        return getLink.map(Link::getCounter).orElse(0);
    }
    @GetMapping("allUrls")
    public List<Link> allUrls(){
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        return linkRepo.findAllByIp(remoteAddress);
    }
}

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
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

    @DeleteMapping("delete/{deleteKey}")
    @Transactional
    public ResponseEntity<Link> deleteLink(@PathVariable("deleteKey")String deleteKey, RedirectAttributes redirectAttributes){
        boolean deleteKeyIsPresent = linkRepo.findAll().stream().anyMatch(k->k.getDeleteKey().equals(deleteKey));
        System.out.println(deleteKeyIsPresent);
        if(deleteKeyIsPresent) {
            linkRepo.deleteByDeleteKey(deleteKey);
            redirectAttributes.addFlashAttribute("delErr", "Link has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            redirectAttributes.addFlashAttribute("delErr", "Invalid delete key");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}

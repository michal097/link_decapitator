package com.example.demo;

import lombok.Getter;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LinkController {

    @Getter
    private final LinkRepo linkRepo;

    @Autowired
    LinkController(LinkRepo linkRepo){
        this.linkRepo=linkRepo;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("link",new Link());
        return "home";

    }
    @PostMapping("/save")
    public String saveAndMakeMagic(@ModelAttribute Link link, RedirectAttributes redirectAttributes){
        String uniqueString = UUID.randomUUID().toString().replaceAll("-","");
        String validatorHTTPS = link.getOriginalName().startsWith("http")?link.getOriginalName():"http://" + link.getOriginalName();
        link.setOriginalName(validatorHTTPS);
        link.setNewName(uniqueString.substring(0,6));
        link.setDeleteKey(uniqueString.substring(28));
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
                link.setIp(remoteAddress);

        if(link.getOriginalName().trim().equals("http://")){
            redirectAttributes.addFlashAttribute("mess","ENTER SOME URL!!!");
            return "redirect:/";
        }
        else {
            linkRepo.save(link);
            redirectAttributes.addFlashAttribute("mess", "localhost:9999/"+link.getNewName());
        }
        return "redirect:/";
    }

    @GetMapping("/{newName}")
    @Transactional
    public RedirectView loadPageFromShort(@PathVariable String newName){

            RedirectView redirectView = new RedirectView();
            Optional<Link> optLink = linkRepo.findByNewName(newName);
            String url ;
            if(optLink.isPresent()) {

                url = optLink.get().getOriginalName();
                linkRepo.increase(optLink.get().getId());
            }else {
                url = "";
            }
            redirectView.setUrl(url);
            return redirectView;
    }

}

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import javax.transaction.Transactional;
import java.util.Optional;


@Controller
public class LinkController {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;

    @Autowired
    LinkController(LinkRepo linkRepo, LinkValidatorService linkValidatorService){
        this.linkRepo=linkRepo;
        this.linkValidatorService=linkValidatorService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("link",new Link());
        return "home";

    }
    @PostMapping("/save")
    public String saveAndMakeMagic(@ModelAttribute Link link, RedirectAttributes redirectAttributes){

        linkValidatorService.makeNewLink(link);

        if(link.getOriginalName().trim().equals("http://")){
            redirectAttributes.addFlashAttribute("err","ENTER SOME URL!!!");
            return "redirect:/";
        }
        else if(!linkValidatorService.checkValid(link.getOriginalName())){
            redirectAttributes.addFlashAttribute("err", "Entered URL is invalid");
            return "redirect:/";
        }

        else {
            linkRepo.save(link);
            redirectAttributes.addFlashAttribute("mess", "s91.herokuapp.com/"+link.getNewName());
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

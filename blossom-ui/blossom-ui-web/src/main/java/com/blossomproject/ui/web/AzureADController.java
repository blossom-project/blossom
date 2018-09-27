package com.blossomproject.ui.web;

import com.blossomproject.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BlossomController
@RequestMapping("/azure/callback")
public class AzureADController {

    @GetMapping
    public String redirectAfterAuthentication(@RequestParam("state") String state){
    return "redirect:"+state;
    }

}

package com.belkin.finch_backend.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("privacy")
public class PrivacyPolicyController {


    @GetMapping
    String getPrivacy() {
        return "privacy.html";
    }
}

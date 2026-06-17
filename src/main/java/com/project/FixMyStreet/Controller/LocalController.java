package com.project.FixMyStreet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/encode")
    public String encode() {
        return passwordEncoder.encode("021105");
    }
}

package com.portal.security.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @GetMapping("/customer")
    public String getCustomer() {
        return "Customer";
    }

    @GetMapping("/ott/sent")
    public String getAdmin() {
        return "go to home page";
    }


}

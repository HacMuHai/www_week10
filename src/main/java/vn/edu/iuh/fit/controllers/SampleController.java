package vn.edu.iuh.fit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/index")
    public String index(){
        return "page index";
    }

    @GetMapping("/home")
    public String home(){
        return "page home";
    }

    @GetMapping("/api")
    public String api(){
        return "page api";
    }

    @GetMapping("/")
    public String test(){
        return "page test";
    }
}

package com.nginvest.neogenesis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {
    @GetMapping("/Neogenesis/investimentos")
    public String page() {
        System.out.println("pagina acessada");
        return "index";
    }
    
}
//http://localhost:8080/Neogenesis/investimentos
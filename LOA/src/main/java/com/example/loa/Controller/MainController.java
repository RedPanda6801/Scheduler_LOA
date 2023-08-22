package com.example.loa.Controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @GetMapping("/api/hello")
    @ResponseBody
    public String test(Model model){
        return "This is Spring's Data";
    }

}

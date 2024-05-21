package show.schedulemanagement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    @GetMapping()
    public String signUp(){
        return "signup/signup";
    }

    @PostMapping()
    public String toComplete(){
        return "redirect:/signup/complete";
    }

    @GetMapping("/complete")
    public String complete(){
        return "signup/complete";
    }
}

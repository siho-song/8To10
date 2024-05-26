package show.schedulemanagement.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import show.schedulemanagement.dto.member.MemberDto;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {
    @GetMapping
    public String home(HttpSession httpSession , Model model){
        MemberDto member = (MemberDto) httpSession.getAttribute("member");
        log.debug("HomeController home() : {}} ", member);
        model.addAttribute("member", member);
        return "home/fullcalendar";
    }
}

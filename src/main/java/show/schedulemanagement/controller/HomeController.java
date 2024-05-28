package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberService memberService;
    @GetMapping
    public String home(Model model){
        Member member = memberService.getAuthenticatedMember();
        log.debug("HomeController home() : {}} ", member);
        model.addAttribute("member", member);
        return "home/fullcalendar";
    }
}

package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/")
    public String loginView() {
        return "login/login";
    }

    //refresh토큰으로 accessToken 재발급
}

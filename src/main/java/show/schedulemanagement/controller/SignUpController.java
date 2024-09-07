package show.schedulemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import show.schedulemanagement.dto.signup.SignUpRequest;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.signup.SignUpService;
import show.schedulemanagement.service.signup.SignUpValidationService;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final SignUpService signUpService;
    private final SignUpValidationService signUpValidationService;
    private final MemberService memberService;

    @GetMapping
    public String getSignUp(@ModelAttribute("dto") SignUpRequest signUpRequest){
        return "signup/signup";
    }

    @GetMapping("/email/exists")
    public ResponseEntity<String> checkDuplicatedEmail(@RequestParam(name = "email") String email){
        if(signUpValidationService.isDuplicatedEmail(email)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nickname/exists")
    public ResponseEntity<String> checkDuplicatedNickName(@RequestParam(name = "nickname") String nickname){
        if(signUpValidationService.isDuplicatedNickname(nickname)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/complete")
    public String complete(@RequestParam(name = "username") String username, Model model){
        model.addAttribute("username", username);
        return "signup/complete";
    }

    @PostMapping
    public String signUp(@ModelAttribute("dto") @Valid SignUpRequest signUpRequest, RedirectAttributes redirectAttributes){
        signUpService.signUp(signUpRequest);
        String username = memberService.findByEmail(signUpRequest.getEmail()).getUsername();
        redirectAttributes.addAttribute("username",username);
        return "redirect:/signup/complete"; //redirect 해야함
    }
}

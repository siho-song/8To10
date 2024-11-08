package show.schedulemanagement.controller;

import static org.springframework.http.HttpStatus.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
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

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
@Slf4j
public class SignUpController {
    private final SignUpService signUpService;

    @GetMapping("/email/exists")
    public ResponseEntity<Boolean> checkEmail(@RequestParam(name = "email") String email){
        Boolean isDuplicatedEmail = signUpService.isDuplicatedEmail(email);
        return new ResponseEntity<>(isDuplicatedEmail, OK);
    }

    @GetMapping("/nickname/exists")
    public ResponseEntity<Boolean> checkNickName(@RequestParam(name = "nickname") String nickname){
        Boolean isDuplicated = signUpService.isDuplicatedNickname(nickname);
        return new ResponseEntity<>(isDuplicated, OK);
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid SignUpRequest signUpRequest){
        signUpService.signUp(signUpRequest);
        return ResponseEntity.noContent().build();
    }
}

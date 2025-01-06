package io.factorialsystems.msscpirateparrotauthorization.controller;

import io.factorialsystems.msscpirateparrotauthorization.dto.RegisterUserDTO;
import io.factorialsystems.msscpirateparrotauthorization.service.JpaUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final JpaUserDetailsService jpaUserDetailsService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

   @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("user")  @Valid RegisterUserDTO user) {
        log.info("Register User {}", user);

        jpaUserDetailsService.createUser(user);

        return "redirect:/login";
    }
}

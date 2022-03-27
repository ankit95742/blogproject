package com.ankit.blog.controller;

import com.ankit.blog.entity.User;
import com.ankit.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        model.addAttribute("authentication",authentication);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "redirect:/login";
    }

}

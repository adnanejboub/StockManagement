package com.stock.controllers;

import com.stock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String role, Model model) {
        try {
            utilisateurService.registerUser(username, password, role);
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/recover")
    public String recover() {
        return "auth/login";
    }

    @PostMapping("/recover")
    public String recoverPassword(@RequestParam String email) {
        return "redirect:/login?recover=attempted";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("successMessage", "Connexion réussie !");
        return "home/index"; // Renders src/main/resources/templates/home/index.html
    }

    @GetMapping("/home/index")
    public String homeIndex(Model model) {
        return "home/index"; // Renders src/main/resources/templates/home/index.html
    }
}
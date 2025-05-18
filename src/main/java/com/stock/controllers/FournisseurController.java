package com.stock.controllers;

import com.stock.entities.Fournisseur;
import com.stock.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurRepository fournisseurRepository;
    @Autowired
    public FournisseurController(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    public record User(String name, String email, String image) {}

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Liste des Fournisseurs");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("useDataTables", true);
        return "fournisseur/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Ajouter un Fournisseur");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("fournisseur", new Fournisseur());
        return "fournisseur/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Fournisseur fournisseur, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Ajouter un Fournisseur");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            return "fournisseur/add";
        }
        fournisseurRepository.save(fournisseur);
        return "redirect:/fournisseurs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur non trouvé"));
        model.addAttribute("pageTitle", "Modifier le Fournisseur");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("fournisseur", fournisseur);
        return "fournisseur/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Fournisseur fournisseur, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Modifier le Fournisseur");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            return "fournisseurs/update";
        }
        fournisseur.setId(id);
        fournisseurRepository.save(fournisseur);
        return "redirect:/fournisseurs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        fournisseurRepository.deleteById(id);
        return "redirect:/fournisseurs";
    }
}
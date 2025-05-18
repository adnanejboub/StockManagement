package com.stock.controllers;

import com.stock.entities.Categorie;
import com.stock.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategoryRepository categorieRepository;

    @Autowired
    public CategorieController(CategoryRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public record User(String name, String email, String image) {}

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Liste des Catégories");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("useDataTables", true);
        return "categorie/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Ajouter une Catégorie");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("categorie", new Categorie());
        return "categorie/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Categorie categorie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Ajouter une Catégorie");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            return "categorie/add";
        }
        categorieRepository.save(categorie);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));
        model.addAttribute("pageTitle", "Modifier la Catégorie");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("categorie", categorie);
        return "categorie/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Categorie categorie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Modifier la Catégorie");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            return "categorie/update";
        }
        categorie.setId(id);
        categorieRepository.save(categorie);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categorieRepository.deleteById(id);
        return "redirect:/categories";
    }
}
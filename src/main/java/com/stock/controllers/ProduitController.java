package com.stock.controllers;

import com.stock.entities.Produit;
import com.stock.repositories.CategoryRepository;
import com.stock.repositories.FournisseurRepository;
import com.stock.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitRepository produitRepository;
    private final CategoryRepository categorieRepository;
    private final FournisseurRepository fournisseurRepository;

    @Autowired
    public ProduitController(ProduitRepository produitRepository,
                             CategoryRepository categorieRepository,
                             FournisseurRepository fournisseurRepository) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    // Simple User record
    public record User(String name, String email, String image) {}

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Liste des Produits");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("produits", produitRepository.findAll());
        model.addAttribute("useDataTables", true);
        return "produit/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Ajouter un Produit");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "produit/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Produit produit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Ajouter un Produit");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            model.addAttribute("categories", categorieRepository.findAll());
            model.addAttribute("fournisseurs", fournisseurRepository.findAll());
            return "produit/form";
        }
        produitRepository.save(produit);
        return "redirect:/produits";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));
        model.addAttribute("pageTitle", "Modifier le Produit");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("produit", produit);
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "produit/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Produit produit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Modifier le Produit");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            model.addAttribute("categories", categorieRepository.findAll());
            model.addAttribute("fournisseurs", fournisseurRepository.findAll());
            return "produits/update";
        }
        produit.setId(id);
        produitRepository.save(produit);
        return "redirect:/produits";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        produitRepository.deleteById(id);
        return "redirect:/produits";
    }
}
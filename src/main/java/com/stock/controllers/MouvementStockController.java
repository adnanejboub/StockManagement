package com.stock.controllers;

import com.stock.entities.MouvementStock;
import com.stock.entities.Produit;
import com.stock.repositories.MouvementStockRepository;
import com.stock.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/mouvements")
public class MouvementStockController {

    private final MouvementStockRepository mouvementStockRepository;
    private final ProduitRepository produitRepository;

    @Autowired
    public MouvementStockController(MouvementStockRepository mouvementStockRepository, ProduitRepository produitRepository) {
        this.mouvementStockRepository = mouvementStockRepository;
        this.produitRepository = produitRepository;
    }

    public record User(String name, String email, String image) {}

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Liste des Mouvements de Stock");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("mouvements", mouvementStockRepository.findAll());
        model.addAttribute("useDataTables", true);
        return "mouvementstock/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Ajouter un Mouvement de Stock");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("mouvement", new MouvementStock());
        model.addAttribute("produits", produitRepository.findAll());
        return "mouvementstock/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("mouvement") MouvementStock mouvement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Ajouter un Mouvement de Stock");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            model.addAttribute("produits", produitRepository.findAll());
            return "mouvementstock/add";
        }
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementStockRepository.save(mouvement);
        updateProductStock(mouvement);
        return "redirect:/mouvements";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MouvementStock mouvement = mouvementStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mouvement non trouvé"));
        model.addAttribute("pageTitle", "Modifier le Mouvement de Stock");
        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
        model.addAttribute("mouvement", mouvement);
        model.addAttribute("produits", produitRepository.findAll());
        return "mouvementstock/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("mouvement") MouvementStock mouvement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Modifier le Mouvement de Stock");
            model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));
            model.addAttribute("produits", produitRepository.findAll());
            return "mouvementstock/update";
        }
        mouvement.setId(id);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementStockRepository.save(mouvement);
        updateProductStock(mouvement);
        return "redirect:/mouvements";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        MouvementStock mouvement = mouvementStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mouvement non trouvé"));
        reverseProductStock(mouvement);
        mouvementStockRepository.deleteById(id);
        return "redirect:/mouvements";
    }

    private void updateProductStock(MouvementStock mouvement) {
        Produit produit = mouvement.getProduit();
        int quantite = mouvement.getQuantite();
        if (mouvement.getTypeMouvement() == MouvementStock.TypeMouvement.ENTREE) {
            produit.setQuantiteStock(produit.getQuantiteStock() + quantite);
        } else {
            produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
        }
        produitRepository.save(produit);
    }

    private void reverseProductStock(MouvementStock mouvement) {
        Produit produit = mouvement.getProduit();
        int quantite = mouvement.getQuantite();
        if (mouvement.getTypeMouvement() == MouvementStock.TypeMouvement.ENTREE) {
            produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
        } else {
            produit.setQuantiteStock(produit.getQuantiteStock() + quantite);
        }
        produitRepository.save(produit);
    }
}
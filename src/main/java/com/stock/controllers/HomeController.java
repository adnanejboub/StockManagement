package com.stock.controllers;

import com.stock.entities.MouvementStock;
import com.stock.entities.Produit;
import com.stock.repositories.CategoryRepository;
import com.stock.repositories.FournisseurRepository;
import com.stock.repositories.MouvementStockRepository;
import com.stock.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class HomeController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategoryRepository categorieRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private MouvementStockRepository mouvementStockRepository;

    public record User(String name, String email, String image) {}

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("pageTitle", "Tableau de Bord - Gestion de Stock");


        model.addAttribute("user", new User("Admin User", "admin@stock.com", "default.jpg"));


        long totalProducts = produitRepository.count();
        model.addAttribute("totalProducts", totalProducts);

        List<Produit> lowStockProductsList = produitRepository.findByQuantiteStockLessThan(10);
        model.addAttribute("lowStockProducts", lowStockProductsList.size());
        model.addAttribute("lowStockProductsList", lowStockProductsList);


        long totalCategories = categorieRepository.count();
        model.addAttribute("totalCategories", totalCategories);


        long totalSuppliers = fournisseurRepository.count();
        model.addAttribute("totalSuppliers", totalSuppliers);


        List<MouvementStock> recentMouvements = mouvementStockRepository.findTop5ByOrderByDateMouvementDesc();
        model.addAttribute("recentMouvements", recentMouvements);

        return "home/index";
    }
}
package com.stock.repositories;

import com.stock.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByQuantiteStockLessThan(int i);
}

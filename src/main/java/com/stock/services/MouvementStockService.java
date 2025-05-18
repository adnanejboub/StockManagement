package com.stock.services;

import com.stock.entities.MouvementStock;
import com.stock.repositories.MouvementStockRepository;
import com.stock.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MouvementStockService {

    @Autowired
    private MouvementStockRepository mouvementStockRepository;


    public List<MouvementStock> findAll() {
        return mouvementStockRepository.findAll();
    }

    public Optional<MouvementStock> findById(Long id) {
        return mouvementStockRepository.findById(id);
    }

    public MouvementStock save(MouvementStock mouvementStock) {
        return mouvementStockRepository.save(mouvementStock);
    }

    public MouvementStock update(Long id, MouvementStock mouvementStock) {
        if (mouvementStockRepository.existsById(id)) {
            mouvementStock.setId(id);
            return mouvementStockRepository.save(mouvementStock);
        }
        return null;
    }

    public void delete(Long id) {
        mouvementStockRepository.deleteById(id);
    }
}
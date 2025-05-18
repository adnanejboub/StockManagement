package com.stock.entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement typeMouvement;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private LocalDateTime dateMouvement = LocalDateTime.now();

    private String commentaire;

    public enum TypeMouvement {
        ENTREE, SORTIE
    }
}
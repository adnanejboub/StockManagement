package com.stock.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fournisseurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String contact;

    private String email;

    private String adresse;
}
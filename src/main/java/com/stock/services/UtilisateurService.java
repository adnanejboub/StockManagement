package com.stock.services;

import com.stock.entities.Utilisateur;
import com.stock.repositories.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé: " + username);
        }
        return utilisateur;
    }

    public void registerUser(String username, String password, String role) {
        if (utilisateurRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Le nom d'utilisateur " + username + " est déjà pris.");
        }
        Utilisateur utilisateur = new Utilisateur(username, passwordEncoder.encode(password), role);
        utilisateurRepository.save(utilisateur);
    }
}
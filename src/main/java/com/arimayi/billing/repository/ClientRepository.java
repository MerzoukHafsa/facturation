package com.arimayi.billing.repository;

import com.arimayi.billing.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour la gestion des clients
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Recherche un client par son email
     */
    Optional<Client> findByEmail(String email);
    
    /**
     * Recherche un client par son SIRET
     */
    Optional<Client> findBySiret(String siret);
    
    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);
    
    /**
     * Vérifie si un SIRET existe déjà
     */
    boolean existsBySiret(String siret);
}

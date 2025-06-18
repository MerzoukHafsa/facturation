package com.arimayi.billing.repository;

import com.arimayi.billing.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour la gestion des factures
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    
    /**
     * Recherche les factures d'un client
     */
    List<Facture> findByClientId(Long clientId);
    
    /**
     * Recherche les factures par date
     */
    List<Facture> findByDate(LocalDate date);
    
    /**
     * Recherche les factures entre deux dates
     */
    List<Facture> findByDateBetween(LocalDate dateDebut, LocalDate dateFin);
    
    /**
     * Recherche les factures d'un client entre deux dates
     */
    @Query("SELECT f FROM Facture f WHERE f.client.id = :clientId AND f.date BETWEEN :dateDebut AND :dateFin")
    List<Facture> findByClientIdAndDateBetween(@Param("clientId") Long clientId, 
                                               @Param("dateDebut") LocalDate dateDebut, 
                                               @Param("dateFin") LocalDate dateFin);
    
    /**
     * Compte le nombre de factures pour générer le numéro
     */
    @Query("SELECT COUNT(f) FROM Facture f WHERE YEAR(f.date) = :annee")
    Long countByYear(@Param("annee") int annee);
}

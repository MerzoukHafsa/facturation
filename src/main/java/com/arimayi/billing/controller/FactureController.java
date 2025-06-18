package com.arimayi.billing.controller;

import com.arimayi.billing.dto.FactureDto;
import com.arimayi.billing.service.FactureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des factures
 */
@RestController
@RequestMapping("/api/factures")
@Tag(name = "Factures", description = "API de gestion des factures")
public class FactureController {
    
    @Autowired
    private FactureService factureService;
    
    /**
     * Récupère toutes les factures
     */
    @GetMapping
    @Operation(summary = "Récupérer toutes les factures", description = "Retourne la liste de toutes les factures")
    public ResponseEntity<List<FactureDto>> getAllFactures() {
        List<FactureDto> factures = factureService.getAllFactures();
        return ResponseEntity.ok(factures);
    }
    
    /**
     * Récupère une facture par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une facture par ID", description = "Retourne les détails d'une facture spécifique")
    public ResponseEntity<FactureDto> getFactureById(@PathVariable Long id) {
        FactureDto facture = factureService.getFactureById(id);
        return ResponseEntity.ok(facture);
    }
    
    /**
     * Crée une nouvelle facture
     */
    @PostMapping
    @Operation(summary = "Créer une nouvelle facture", description = "Crée une nouvelle facture avec calcul automatique des totaux")
    public ResponseEntity<FactureDto> createFacture(@Valid @RequestBody FactureDto factureDto) {
        FactureDto createdFacture = factureService.createFacture(factureDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFacture);
    }
    
    /**
     * Recherche les factures par client
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Rechercher les factures par client", description = "Retourne toutes les factures d'un client spécifique")
    public ResponseEntity<List<FactureDto>> getFacturesByClient(@PathVariable Long clientId) {
        List<FactureDto> factures = factureService.getFacturesByClient(clientId);
        return ResponseEntity.ok(factures);
    }
    
    /**
     * Recherche les factures par date
     */
    @GetMapping("/date/{date}")
    @Operation(summary = "Rechercher les factures par date", description = "Retourne toutes les factures d'une date spécifique")
    public ResponseEntity<List<FactureDto>> getFacturesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FactureDto> factures = factureService.getFacturesByDate(date);
        return ResponseEntity.ok(factures);
    }
    
    /**
     * Recherche les factures entre deux dates
     */
    @GetMapping("/periode")
    @Operation(summary = "Rechercher les factures par période", description = "Retourne toutes les factures entre deux dates")
    public ResponseEntity<List<FactureDto>> getFacturesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<FactureDto> factures = factureService.getFacturesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(factures);
    }
    
    /**
     * Exporte une facture au format JSON
     */
    @GetMapping("/{id}/export")
    @Operation(summary = "Exporter une facture en JSON", description = "Retourne une facture complète au format JSON")
    public ResponseEntity<FactureDto> exportFactureJson(@PathVariable Long id) {
        FactureDto facture = factureService.exportFactureJson(id);
        return ResponseEntity.ok(facture);
    }
}

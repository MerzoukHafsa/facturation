package com.arimayi.billing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une facture
 */
@Entity
@Table(name = "factures")
public class Facture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    @NotNull(message = "La date de facture est obligatoire")
    @Column(nullable = false)
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneFacture> lignes = new ArrayList<>();
    
    @Column(name = "total_ht", precision = 10, scale = 2)
    private BigDecimal totalHT;
    
    @Column(name = "total_tva", precision = 10, scale = 2)
    private BigDecimal totalTVA;
    
    @Column(name = "total_ttc", precision = 10, scale = 2)
    private BigDecimal totalTTC;
    
    // Constructeurs
    public Facture() {}
    
    public Facture(LocalDate date, Client client) {
        this.date = date;
        this.client = client;
        this.totalHT = BigDecimal.ZERO;
        this.totalTVA = BigDecimal.ZERO;
        this.totalTTC = BigDecimal.ZERO;
    }
    
    /**
     * Calcule automatiquement les totaux de la facture
     */
    public void calculerTotaux() {
        this.totalHT = lignes.stream()
            .map(LigneFacture::getTotalHT)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        this.totalTVA = lignes.stream()
            .map(LigneFacture::getTotalTVA)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        this.totalTTC = totalHT.add(totalTVA);
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public List<LigneFacture> getLignes() { return lignes; }
    public void setLignes(List<LigneFacture> lignes) { this.lignes = lignes; }
    
    public BigDecimal getTotalHT() { return totalHT; }
    public void setTotalHT(BigDecimal totalHT) { this.totalHT = totalHT; }
    
    public BigDecimal getTotalTVA() { return totalTVA; }
    public void setTotalTVA(BigDecimal totalTVA) { this.totalTVA = totalTVA; }
    
    public BigDecimal getTotalTTC() { return totalTTC; }
    public void setTotalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; }
}

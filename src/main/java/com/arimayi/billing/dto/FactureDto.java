package com.arimayi.billing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO pour les factures
 */
public class FactureDto {
    
    private Long id;
    private String numero;
    
    @NotNull(message = "La date de facture est obligatoire")
    private LocalDate date;
    
    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    
    private ClientDto client;
    
    @NotEmpty(message = "Une facture doit avoir au moins une ligne")
    @Valid
    private List<LigneFactureDto> lignes;
    
    private BigDecimal totalHT;
    private BigDecimal totalTVA;
    private BigDecimal totalTTC;
    
    // Constructeurs
    public FactureDto() {}
    
    public FactureDto(LocalDate date, Long clientId, List<LigneFactureDto> lignes) {
        this.date = date;
        this.clientId = clientId;
        this.lignes = lignes;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public ClientDto getClient() { return client; }
    public void setClient(ClientDto client) { this.client = client; }
    
    public List<LigneFactureDto> getLignes() { return lignes; }
    public void setLignes(List<LigneFactureDto> lignes) { this.lignes = lignes; }
    
    public BigDecimal getTotalHT() { return totalHT; }
    public void setTotalHT(BigDecimal totalHT) { this.totalHT = totalHT; }
    
    public BigDecimal getTotalTVA() { return totalTVA; }
    public void setTotalTVA(BigDecimal totalTVA) { this.totalTVA = totalTVA; }
    
    public BigDecimal getTotalTTC() { return totalTTC; }
    public void setTotalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; }
}

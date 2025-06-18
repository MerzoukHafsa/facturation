package com.arimayi.billing.service;

import com.arimayi.billing.dto.ClientDto;
import com.arimayi.billing.dto.FactureDto;
import com.arimayi.billing.dto.LigneFactureDto;
import com.arimayi.billing.entity.Client;
import com.arimayi.billing.entity.Facture;
import com.arimayi.billing.entity.LigneFacture;
import com.arimayi.billing.exception.ResourceNotFoundException;
import com.arimayi.billing.exception.InvalidTVAException;
import com.arimayi.billing.repository.ClientRepository;
import com.arimayi.billing.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des factures
 */
@Service
@Transactional
public class FactureService {
    
    @Autowired
    private FactureRepository factureRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    // Taux de TVA autorisés
    private static final List<BigDecimal> TAUX_TVA_AUTORISES = Arrays.asList(
        BigDecimal.ZERO,
        new BigDecimal("5.5"),
        new BigDecimal("10"),
        new BigDecimal("20")
    );
    
    /**
     * Récupère toutes les factures
     */
    @Transactional(readOnly = true)
    public List<FactureDto> getAllFactures() {
        return factureRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère une facture par son ID
     */
    @Transactional(readOnly = true)
    public FactureDto getFactureById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id));
        return convertToDto(facture);
    }
    
    /**
     * Crée une nouvelle facture
     */
    public FactureDto createFacture(FactureDto factureDto) {
        // Vérification de l'existence du client
        Client client = clientRepository.findById(factureDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + factureDto.getClientId()));
        
        // Validation des taux de TVA
        validateTauxTVA(factureDto.getLignes());
        
        // Création de la facture
        Facture facture = new Facture(factureDto.getDate(), client);
        facture.setNumero(generateNumeroFacture(factureDto.getDate()));
        
        // Ajout des lignes
        List<LigneFacture> lignes = factureDto.getLignes().stream()
                .map(ligneDto -> {
                    LigneFacture ligne = convertLigneDtoToEntity(ligneDto);
                    ligne.setFacture(facture);
                    return ligne;
                })
                .collect(Collectors.toList());
        
        facture.setLignes(lignes);
        facture.calculerTotaux();
        
        Facture savedFacture = factureRepository.save(facture);
        return convertToDto(savedFacture);
    }
    
    /**
     * Recherche les factures par client
     */
    @Transactional(readOnly = true)
    public List<FactureDto> getFacturesByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId);
        }
        
        return factureRepository.findByClientId(clientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche les factures par date
     */
    @Transactional(readOnly = true)
    public List<FactureDto> getFacturesByDate(LocalDate date) {
        return factureRepository.findByDate(date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche les factures entre deux dates
     */
    @Transactional(readOnly = true)
    public List<FactureDto> getFacturesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return factureRepository.findByDateBetween(dateDebut, dateFin).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Exporte une facture au format JSON
     */
    @Transactional(readOnly = true)
    public FactureDto exportFactureJson(Long id) {
        return getFactureById(id);
    }
    
    /**
     * Génère un numéro de facture unique
     */
    private String generateNumeroFacture(LocalDate date) {
        int annee = date.getYear();
        Long count = factureRepository.countByYear(annee);
        return String.format("FAC-%d-%04d", annee, count + 1);
    }
    
    /**
     * Valide les taux de TVA
     */
    private void validateTauxTVA(List<LigneFactureDto> lignes) {
        for (LigneFactureDto ligne : lignes) {
            if (!TAUX_TVA_AUTORISES.contains(ligne.getTauxTVA())) {
                throw new InvalidTVAException("Taux de TVA non autorisé: " + ligne.getTauxTVA() + 
                    ". Taux autorisés: 0%, 5.5%, 10%, 20%");
            }
        }
    }
    
    /**
     * Convertit une entité Facture en DTO
     */
    private FactureDto convertToDto(Facture facture) {
        FactureDto dto = new FactureDto();
        dto.setId(facture.getId());
        dto.setNumero(facture.getNumero());
        dto.setDate(facture.getDate());
        dto.setClientId(facture.getClient().getId());
        dto.setClient(convertClientToDto(facture.getClient()));
        dto.setTotalHT(facture.getTotalHT());
        dto.setTotalTVA(facture.getTotalTVA());
        dto.setTotalTTC(facture.getTotalTTC());
        
        List<LigneFactureDto> lignesDto = facture.getLignes().stream()
                .map(this::convertLigneEntityToDto)
                .collect(Collectors.toList());
        dto.setLignes(lignesDto);
        
        return dto;
    }
    
    /**
     * Convertit une entité Client en DTO
     */
    private ClientDto convertClientToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setSiret(client.getSiret());
        dto.setDateCreation(client.getDateCreation());
        return dto;
    }
    
    /**
     * Convertit une entité LigneFacture en DTO
     */
    private LigneFactureDto convertLigneEntityToDto(LigneFacture ligne) {
        LigneFactureDto dto = new LigneFactureDto();
        dto.setId(ligne.getId());
        dto.setDescription(ligne.getDescription());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaireHT(ligne.getPrixUnitaireHT());
        dto.setTauxTVA(ligne.getTauxTVA());
        dto.setTotalHT(ligne.getTotalHT());
        dto.setTotalTVA(ligne.getTotalTVA());
        dto.setTotalTTC(ligne.getTotalTTC());
        return dto;
    }
    
    /**
     * Convertit un DTO LigneFacture en entité
     */
    private LigneFacture convertLigneDtoToEntity(LigneFactureDto dto) {
        LigneFacture ligne = new LigneFacture();
        ligne.setDescription(dto.getDescription());
        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaireHT(dto.getPrixUnitaireHT());
        ligne.setTauxTVA(dto.getTauxTVA());
        return ligne;
    }
}

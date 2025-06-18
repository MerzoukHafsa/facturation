package com.arimayi.billing.service;

import com.arimayi.billing.dto.FactureDto;
import com.arimayi.billing.dto.LigneFactureDto;
import com.arimayi.billing.entity.Client;
import com.arimayi.billing.entity.Facture;
import com.arimayi.billing.exception.ResourceNotFoundException;
import com.arimayi.billing.exception.InvalidTVAException;
import com.arimayi.billing.repository.ClientRepository;
import com.arimayi.billing.repository.FactureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour FactureService
 */
@ExtendWith(MockitoExtension.class)
class FactureServiceTest {
    
    @Mock
    private FactureRepository factureRepository;
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private FactureService factureService;
    
    private Client testClient;
    private FactureDto testFactureDto;
    
    @BeforeEach
    void setUp() {
        // Création d'un client de test
        testClient = new Client("Test Client", "test@example.com", "12345678901234");
        testClient.setId(1L);
        
        // Création d'une ligne de facture de test
        LigneFactureDto ligneDto = new LigneFactureDto();
        ligneDto.setDescription("Service de test");
        ligneDto.setQuantite(new BigDecimal("2"));
        ligneDto.setPrixUnitaireHT(new BigDecimal("100.00"));
        ligneDto.setTauxTVA(new BigDecimal("20"));
        
        // Création d'une facture de test
        testFactureDto = new FactureDto();
        testFactureDto.setDate(LocalDate.now());
        testFactureDto.setClientId(1L);
        testFactureDto.setLignes(Arrays.asList(ligneDto));
    }
    
    @Test
    void createFacture_WithValidData_ShouldReturnFactureDto() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(factureRepository.countByYear(anyInt())).thenReturn(0L);
        when(factureRepository.save(any(Facture.class))).thenAnswer(invocation -> {
            Facture facture = invocation.getArgument(0);
            facture.setId(1L);
            return facture;
        });
        
        // When
        FactureDto result = factureService.createFacture(testFactureDto);
        
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getNumero());
        assertEquals(new BigDecimal("200.00"), result.getTotalHT());
        assertEquals(new BigDecimal("40.00"), result.getTotalTVA());
        assertEquals(new BigDecimal("240.00"), result.getTotalTTC());
        
        verify(clientRepository).findById(1L);
        verify(factureRepository).save(any(Facture.class));
    }
    
    @Test
    void createFacture_WithInvalidClient_ShouldThrowException() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            factureService.createFacture(testFactureDto);
        });
        
        verify(clientRepository).findById(1L);
        verify(factureRepository, never()).save(any(Facture.class));
    }
    
    @Test
    void createFacture_WithInvalidTVA_ShouldThrowException() {
        // Given
        testFactureDto.getLignes().get(0).setTauxTVA(new BigDecimal("15")); // Taux invalide
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        
        // When & Then
        assertThrows(InvalidTVAException.class, () -> {
            factureService.createFacture(testFactureDto);
        });
        
        verify(clientRepository).findById(1L);
        verify(factureRepository, never()).save(any(Facture.class));
    }
    
    @Test
    void getFactureById_WithValidId_ShouldReturnFactureDto() {
        // Given
        Facture facture = new Facture(LocalDate.now(), testClient);
        facture.setId(1L);
        facture.setNumero("FAC-2024-0001");
        
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));
        
        // When
        FactureDto result = factureService.getFactureById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("FAC-2024-0001", result.getNumero());
        
        verify(factureRepository).findById(1L);
    }
    
    @Test
    void getFactureById_WithInvalidId_ShouldThrowException() {
        // Given
        when(factureRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            factureService.getFactureById(1L);
        });
        
        verify(factureRepository).findById(1L);
    }
}

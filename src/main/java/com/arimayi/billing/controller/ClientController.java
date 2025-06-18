package com.arimayi.billing.controller;

import com.arimayi.billing.dto.ClientDto;
import com.arimayi.billing.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des clients
 */
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "API de gestion des clients")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    /**
     * Récupère tous les clients
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les clients", description = "Retourne la liste de tous les clients")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    
    /**
     * Récupère un client par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par ID", description = "Retourne les détails d'un client spécifique")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }
    
    /**
     * Crée un nouveau client
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau client", description = "Crée un nouveau client avec les informations fournies")
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        ClientDto createdClient = clientService.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }
    
    /**
     * Met à jour un client existant
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un client", description = "Met à jour les informations d'un client existant")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        ClientDto updatedClient = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok(updatedClient);
    }
    
    /**
     * Supprime un client
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client existant")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}

package com.arimayi.billing.service;

import com.arimayi.billing.dto.ClientDto;
import com.arimayi.billing.entity.Client;
import com.arimayi.billing.exception.ResourceNotFoundException;
import com.arimayi.billing.exception.DuplicateResourceException;
import com.arimayi.billing.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des clients
 */
@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    /**
     * Récupère tous les clients
     */
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un client par son ID
     */
    @Transactional(readOnly = true)
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        return convertToDto(client);
    }
    
    /**
     * Crée un nouveau client
     */
    public ClientDto createClient(ClientDto clientDto) {
        // Vérification de l'unicité de l'email
        if (clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe déjà: " + clientDto.getEmail());
        }
        
        // Vérification de l'unicité du SIRET
        if (clientRepository.existsBySiret(clientDto.getSiret())) {
            throw new DuplicateResourceException("Un client avec ce SIRET existe déjà: " + clientDto.getSiret());
        }
        
        Client client = convertToEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return convertToDto(savedClient);
    }
    
    /**
     * Met à jour un client existant
     */
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        
        // Vérification de l'unicité de l'email (sauf pour le client actuel)
        if (!existingClient.getEmail().equals(clientDto.getEmail()) && 
            clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe déjà: " + clientDto.getEmail());
        }
        
        // Vérification de l'unicité du SIRET (sauf pour le client actuel)
        if (!existingClient.getSiret().equals(clientDto.getSiret()) && 
            clientRepository.existsBySiret(clientDto.getSiret())) {
            throw new DuplicateResourceException("Un client avec ce SIRET existe déjà: " + clientDto.getSiret());
        }
        
        existingClient.setNom(clientDto.getNom());
        existingClient.setEmail(clientDto.getEmail());
        existingClient.setSiret(clientDto.getSiret());
        
        Client updatedClient = clientRepository.save(existingClient);
        return convertToDto(updatedClient);
    }
    
    /**
     * Supprime un client
     */
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }
    
    /**
     * Convertit une entité Client en DTO
     */
    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setSiret(client.getSiret());
        dto.setDateCreation(client.getDateCreation());
        return dto;
    }
    
    /**
     * Convertit un DTO en entité Client
     */
    private Client convertToEntity(ClientDto dto) {
        Client client = new Client();
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        client.setSiret(dto.getSiret());
        return client;
    }
}

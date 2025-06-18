package com.arimayi.billing.controller;

import com.arimayi.billing.dto.ClientDto;
import com.arimayi.billing.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour ClientController
 */
@WebMvcTest(ClientController.class)
class ClientControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ClientService clientService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private ClientDto testClientDto;
    
    @BeforeEach
    void setUp() {
        testClientDto = new ClientDto();
        testClientDto.setId(1L);
        testClientDto.setNom("Test Client");
        testClientDto.setEmail("test@example.com");
        testClientDto.setSiret("12345678901234");
        testClientDto.setDateCreation(LocalDateTime.now());
    }
    
    @Test
    @WithMockUser
    void getAllClients_ShouldReturnClientsList() throws Exception {
        // Given
        List<ClientDto> clients = Arrays.asList(testClientDto);
        when(clientService.getAllClients()).thenReturn(clients);
        
        // When & Then
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Test Client"));
    }
    
    @Test
    @WithMockUser
    void createClient_WithValidData_ShouldReturnCreatedClient() throws Exception {
        // Given
        ClientDto newClientDto = new ClientDto("New Client", "new@example.com", "98765432109876");
        when(clientService.createClient(any(ClientDto.class))).thenReturn(testClientDto);
        
        // When & Then
        mockMvc.perform(post("/api/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newClientDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Test Client"));
    }
}

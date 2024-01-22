package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    List<ClientDTO> getAllClientDTO();

    Client getClientById(Long id);

    ClientDTO getClientDTOById(Long id);

    Client getClientByEmail(String email);

    ClientDTO getClientDTOByEmail(String email);

    ClientDTO getAuthenticatedClientDTO(Authentication authentication);
    ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password);

    void saveToRepository(Client client);
}

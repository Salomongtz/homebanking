package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    public List<Client> getAllClients();

    public List<ClientDTO> getAllClientDTO();

    public Client getClientById(Long id);

    public ClientDTO getClientDTOById(Long id);

    public Client getClientByEmail(String email);

    public ClientDTO getClientDTOByEmail(String email);

    public ClientDTO getAuthenticatedClientDTO(Authentication authentication);

    public void saveToRepository(Client client);
}

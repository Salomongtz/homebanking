package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return getAllClients().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        return getClientById(id) != null ? new ClientDTO(getClientById(id)) : null;
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientDTO getClientDTOByEmail(String email) {
        return getClientByEmail(email) != null ? new ClientDTO(getClientByEmail(email)) : null;
    }

    @Override
    public ClientDTO getAuthenticatedClientDTO(Authentication authentication) {
        return clientRepository.existsByEmail(authentication.getName()) ?
                new ClientDTO(clientRepository.findByEmail(authentication.getName())) : null;
    }

    @Override
    public void saveToRepository(Client client) {
        clientRepository.save(client);
    }
}

package ru.neoflex.neosudy.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neosudy.deal.mapper.ClientMapper;
import ru.neoflex.neosudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neosudy.deal.model.entity.Client;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper clientMapper;

    public Client getClientFromLoanApplicationRequestDTO(LoanApplicationRequestDTO requestDTO) {
        return clientMapper.loanApplicationRequestToClient(requestDTO);
    }

    public void updateClientFromFinishRegistrationRequestDTO(Client client, FinishRegistrationRequestDTO request) {
        clientMapper.updateClient(client, request);
    }

    public ScoringDataDTO getScoringDataDTOFromClient(Client client) {
        return clientMapper.clientToScoringData(client);
    }
}

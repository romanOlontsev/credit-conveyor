package ru.neoflex.neostudy.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.deal.mapper.ClientMapper;
import ru.neoflex.neostudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;

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

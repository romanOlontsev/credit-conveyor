package ru.neoflex.neosudy.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neosudy.deal.exception.DataNotFoundException;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.entity.Application;
import ru.neoflex.neosudy.deal.model.entity.Client;
import ru.neoflex.neosudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neosudy.deal.model.types.ApplicationStatus;
import ru.neoflex.neosudy.deal.model.types.ChangeType;
import ru.neoflex.neosudy.deal.repository.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ClientService clientService;

    public Application createApplication(LoanApplicationRequestDTO request) {
        log.info("LoanApplicationRequestDTO input: {}", request);
        Client client = clientService.getClientFromLoanApplicationRequestDTO(request);
        ApplicationStatus status = ApplicationStatus.PREAPPROVAL;
        Application application = Application.builder()
                                             .clientId(client)
                                             .build();
        changeStatus(application, status);
        log.info("Client saved: {}", client);
        return applicationRepository.save(application);
    }

    public Application findApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                                    .orElseThrow(() -> new DataNotFoundException("Application with id=" +
                                                                                         applicationId + " not found"));
    }

    public void changeStatus(Application application, ApplicationStatus status) {
        LocalDateTime creationDate = LocalDateTime.now();
        StatusHistory statusHistory = StatusHistory.builder()
                                                   .status(status)
                                                   .time(creationDate)
                                                   .changeType(ChangeType.AUTOMATIC)
                                                   .build();
        application.setStatus(status);
        application.setCreationDate(creationDate);
        if (application.getStatusHistory() == null) {
            application.setStatusHistory(List.of(statusHistory));
        } else {
            application.getStatusHistory()
                       .add(statusHistory);
        }
    }
}

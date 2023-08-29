package ru.neoflex.neostudy.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.deal.mapper.ApplicationMapper;
import ru.neoflex.neostudy.deal.model.entity.Application;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neostudy.deal.model.dto.ApplicationDTO;
import ru.neoflex.neostudy.deal.model.types.ChangeType;
import ru.neoflex.neostudy.deal.exception.DataNotFoundException;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.deal.model.types.ApplicationStatus;
import ru.neoflex.neostudy.deal.repository.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationMapper applicationMapper;
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

    public ApplicationDTO getApplicationInfoFromApplication(Application application) {
        return applicationMapper.applicationToApplicationInfo(application);
    }
}

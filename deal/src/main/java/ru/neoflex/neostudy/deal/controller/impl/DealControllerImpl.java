package ru.neoflex.neostudy.deal.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.deal.controller.DealController;
import ru.neoflex.neostudy.deal.model.dto.ApplicationDTO;
import ru.neoflex.neostudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final DealService service;

    @Override
    public List<LoanOfferDTO> calculateOffers(LoanApplicationRequestDTO request) {
        return service.getOffers(request);
    }

    @Override
    public void selectOffer(LoanOfferDTO request) {
        service.selectOffer(request);
    }

    @Override
    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO request) {
        service.finishRegistration(applicationId, request);
    }

    @Override
    public void sendDocuments(Long applicationId) {
        service.sendDocuments(applicationId);
    }

    @Override
    public void signDocuments(Long applicationId) {
        service.signDocuments(applicationId);
    }

    @Override
    public void verifySesCode(Long applicationId) {
        service.verifySesCode(applicationId);
    }

    @Override
    public void updateApplicationStatus(Long applicationId) {
        service.updateApplicationStatus(applicationId);
    }

    @Override
    public ApplicationDTO getApplicationById(Long id) {
        return service.getApplicationById(id);
    }

    @Override
    public List<ApplicationDTO> getAllApplications() {
        return service.getAllApplications();
    }
}

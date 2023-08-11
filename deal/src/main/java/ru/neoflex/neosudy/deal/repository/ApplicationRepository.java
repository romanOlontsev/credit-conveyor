package ru.neoflex.neosudy.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.neosudy.deal.model.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
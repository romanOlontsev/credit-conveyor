package ru.neoflex.neostudy.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.neostudy.deal.model.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
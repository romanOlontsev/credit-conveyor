package ru.neoflex.neosudy.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.neosudy.deal.model.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}

package ru.neoflex.neosudy.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.neosudy.deal.model.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

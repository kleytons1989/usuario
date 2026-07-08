package com.kleyton.aprendendo.spring.infrastructure.repository;

import com.kleyton.aprendendo.spring.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}

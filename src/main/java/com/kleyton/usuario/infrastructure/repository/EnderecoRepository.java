package com.kleyton.usuario.infrastructure.repository;

import com.kleyton.aprendendo.spring.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}

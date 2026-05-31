package org.eskilokos.eskilokos.features.Cliente.repository;

import org.eskilokos.eskilokos.core.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}

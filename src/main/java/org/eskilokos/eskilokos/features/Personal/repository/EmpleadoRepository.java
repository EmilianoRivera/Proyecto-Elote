package org.eskilokos.eskilokos.features.Personal.repository;

import org.eskilokos.eskilokos.core.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
}
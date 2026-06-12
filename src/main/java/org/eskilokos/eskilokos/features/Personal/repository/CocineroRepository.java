package org.eskilokos.eskilokos.features.Personal.repository;

import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocineroRepository extends JpaRepository<Cocinero, Integer> {
    List<Cocinero> findByEmpleado_NombreContainingIgnoreCase(String nombre);}
package org.eskilokos.eskilokos.features.Inventario.repository;

import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    List<Inventario> findByNombreContainingIgnoreCase(String nombre);
}

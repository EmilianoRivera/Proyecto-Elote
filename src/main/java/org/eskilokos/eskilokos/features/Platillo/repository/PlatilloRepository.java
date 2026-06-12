package org.eskilokos.eskilokos.features.Platillo.repository;

import org.eskilokos.eskilokos.core.entidades.Platillo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlatilloRepository extends JpaRepository<Platillo, Integer> {
    List<Platillo> findByNombreContainingIgnoreCase(String nombre);
}

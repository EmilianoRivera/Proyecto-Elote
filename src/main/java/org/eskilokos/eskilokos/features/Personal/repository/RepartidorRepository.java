package org.eskilokos.eskilokos.features.Personal.repository;

import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Integer> {
    // Muy útil para cuando necesiten asignar un pedido a un repartidor libre
    List<Repartidor> findByEntregasDispGreaterThan(Integer entregasDisp);
}
package org.eskilokos.eskilokos.features.Personal.repository;

import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Integer> {

    // Filtro para conductores activos/disponibles
    List<Repartidor> findByEntregasDispGreaterThan(Integer entregas);

    // Método clave para buscar repartidores cruzando la relación con Empleado
    List<Repartidor> findByEmpleado_NombreContainingIgnoreCase(String nombre);
}
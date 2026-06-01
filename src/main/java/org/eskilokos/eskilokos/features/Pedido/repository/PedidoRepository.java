package org.eskilokos.eskilokos.features.Pedido.repository;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente_IdCliente(Integer idCliente);
}
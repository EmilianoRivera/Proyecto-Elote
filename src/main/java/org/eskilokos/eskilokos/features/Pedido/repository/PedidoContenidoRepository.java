package org.eskilokos.eskilokos.features.Pedido.repository;

import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoContenidoRepository extends JpaRepository<PedidoContenido, Integer> {
    // Este método es el que busca todos los elotes/esquites de una sola orden
    List<PedidoContenido> findByPedido_IdPedido(Integer idPedido);
}
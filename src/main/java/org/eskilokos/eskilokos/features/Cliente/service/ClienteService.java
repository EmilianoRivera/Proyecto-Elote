package org.eskilokos.eskilokos.features.Cliente.service;

import org.eskilokos.eskilokos.core.entidades.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> findAll();

    Cliente findById(Integer id);

    List<Cliente> buscarPorNombre(String nombre); // Nuevo contrato añadido

    Cliente save(Cliente cliente);

    Cliente update(Integer id, Cliente cliente); // Nuevo contrato añadido para actualización completa (PUT)

    void deleteById(Integer id);
}
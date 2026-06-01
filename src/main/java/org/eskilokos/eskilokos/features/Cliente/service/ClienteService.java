package org.eskilokos.eskilokos.features.Cliente.service;

import org.eskilokos.eskilokos.core.entidades.Cliente;

import java.util.List;

public interface ClienteService {
    public List<Cliente> findAll();
    public Cliente findById(Integer id);
    public Cliente save(Cliente cliente);
    public void deleteById(Integer id);
}

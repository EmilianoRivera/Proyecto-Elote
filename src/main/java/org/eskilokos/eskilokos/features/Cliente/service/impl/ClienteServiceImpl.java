package org.eskilokos.eskilokos.features.Cliente.service.impl;

import org.eskilokos.eskilokos.core.entidades.Cliente;
import org.eskilokos.eskilokos.features.Cliente.repository.ClienteRepository;
import org.eskilokos.eskilokos.features.Cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente No Econtrado"));
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente actualizar(Integer id, Cliente datos) {
        Cliente existente = findById(id);
        if (datos.getNombre() != null)
            existente.setNombre(datos.getNombre());
        if (datos.getTelefono() != null)
            existente.setTelefono(datos.getTelefono());
        if (datos.getEmail() != null)
            existente.setEmail(datos.getEmail());
        return clienteRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("No se puede Eliminar, No existe");
        }
        clienteRepository.deleteById(id);
    }
}

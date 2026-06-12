package org.eskilokos.eskilokos.features.Cliente.service.impl;

import org.eskilokos.eskilokos.core.entidades.Cliente;
import org.eskilokos.eskilokos.features.Cliente.repository.ClienteRepository;
import org.eskilokos.eskilokos.features.Cliente.service.ClienteService;
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
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente No Encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente update(Integer id, Cliente datosNuevos) {
        Cliente clienteExistente = findById(id);

        // Sincronizamos las propiedades editables del cliente
        clienteExistente.setNombre(datosNuevos.getNombre());
        clienteExistente.setEmail(datosNuevos.getEmail());
        clienteExistente.setTelefono(datosNuevos.getTelefono());

        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("No se puede Eliminar, No existe");
        }
        clienteRepository.deleteById(id);
    }
}
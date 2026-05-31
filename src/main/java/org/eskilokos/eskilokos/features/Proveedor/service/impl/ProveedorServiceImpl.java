package org.eskilokos.eskilokos.features.Proveedor.service.impl;

import lombok.RequiredArgsConstructor;
import org.eskilokos.eskilokos.core.entidades.Proveedor;
import org.eskilokos.eskilokos.features.Proveedor.repository.ProveedorRepository;
import org.eskilokos.eskilokos.features.Proveedor.service.ProveedorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> obtenerPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Integer id, Proveedor proveedor) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Proveedor con id " + id + " no encontrado"));

        existente.setNombre(proveedor.getNombre());
        existente.setDireccion(proveedor.getDireccion());
        existente.setTelefono(proveedor.getTelefono());
        existente.setEmail(proveedor.getEmail());
        existente.setDescripcion(proveedor.getDescripcion());

        return proveedorRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new NoSuchElementException("Proveedor con id " + id + " no encontrado");
        }
        proveedorRepository.deleteById(id);
    }
}
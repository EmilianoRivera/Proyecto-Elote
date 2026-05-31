package org.eskilokos.eskilokos.features.Proveedor.service;

import org.eskilokos.eskilokos.core.entidades.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> obtenerTodos();

    Optional<Proveedor> obtenerPorId(Integer id);

    List<Proveedor> buscarPorNombre(String nombre);

    Proveedor guardar(Proveedor proveedor);

    Proveedor actualizar(Integer id, Proveedor proveedor);

    void eliminar(Integer id);
}
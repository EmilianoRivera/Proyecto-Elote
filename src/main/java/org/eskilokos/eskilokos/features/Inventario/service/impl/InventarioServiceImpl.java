package org.eskilokos.eskilokos.features.Inventario.service.impl;

import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.eskilokos.eskilokos.features.Inventario.repository.InventarioRepository; // Importación corregida
import org.eskilokos.eskilokos.features.Inventario.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listaTodosLosInsumos() {
        return inventarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventario findInsumoById(Integer id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo No Encontrado con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> buscarInsumosPorNombre(String nombre) {
        return inventarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Inventario registrarInsumo(Inventario insumo) {
        if (insumo.getCantidad() < 0) {
            throw new IllegalArgumentException("No puede tener Stock Negativo al registrar");
        }
        return inventarioRepository.save(insumo);
    }

    @Override
    @Transactional
    public Inventario actualizarStock(Integer id, int cantidadAjustada) {
        Inventario insumo = findInsumoById(id);
        int nuevaCantidad = insumo.getCantidad() + cantidadAjustada;

        if (nuevaCantidad < 0) {
            throw new RuntimeException("El ajuste dejaría el Stock en números negativos");
        }
        insumo.setCantidad(nuevaCantidad);
        return inventarioRepository.save(insumo);
    }

    @Override
    @Transactional
    public Inventario actualizarInsumoCompleto(Integer id, Inventario datosNuevos) {
        Inventario insumoExistente = findInsumoById(id);

        insumoExistente.setNombre(datosNuevos.getNombre());
        insumoExistente.setCantidad(datosNuevos.getCantidad());
        insumoExistente.setCaducidad(datosNuevos.getCaducidad());

        if (insumoExistente.getCantidad() < 0) {
            throw new IllegalArgumentException("El stock final no puede ser negativo");
        }

        return inventarioRepository.save(insumoExistente);
    }

    @Override
    @Transactional
    public void eliminarInsumo(Integer id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("No existe el Insumo");
        }
        inventarioRepository.deleteById(id);
    }
}
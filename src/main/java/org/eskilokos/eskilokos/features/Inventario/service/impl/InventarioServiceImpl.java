package org.eskilokos.eskilokos.features.Inventario.service.impl;

import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.eskilokos.eskilokos.features.Inventario.repository.InventarioRepository;
import org.eskilokos.eskilokos.features.Inventario.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService{
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
                .orElseThrow(()-> new RuntimeException("Insumo No Encontrado"));
    }

    @Override
    @Transactional
    public Inventario registrarInsumo(Inventario insumo) {
        if (insumo.getCantidad() < 0){
            throw new IllegalArgumentException("No puede tener Stock Negativo");
        } else
            return inventarioRepository.save(insumo);
    }


    @Override
    public void eliminarInsumo(Integer id) {
        if (!inventarioRepository.existsById(id)){
            throw new RuntimeException("No existe el Insumo");
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Inventario agregarStock(Integer id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a sumar debe ser mayor a cero");
        }
        Inventario insumo = findInsumoById(id);
        insumo.setCantidad(insumo.getCantidad() + cantidad);
        return inventarioRepository.save(insumo);
    }

    @Override
    @Transactional
    public Inventario descontarStock(Integer id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero");
        }
        Inventario insumo = findInsumoById(id);
        int nuevaCantidad = insumo.getCantidad() - cantidad;

        if (nuevaCantidad < 0) {
            throw new RuntimeException("Stock insuficiente. No se puede descontar " + cantidad + " de " + insumo.getNombre());
        }
        insumo.setCantidad(nuevaCantidad);
        return inventarioRepository.save(insumo);
    }

    @Override
    @Transactional
    public Inventario fijarStockManual(Integer id, int cantidadExacta) {
        if (cantidadExacta < 0) {
            throw new IllegalArgumentException("El stock físico no puede ser negativo");
        }
        Inventario insumo = findInsumoById(id);
        insumo.setCantidad(cantidadExacta);
        return inventarioRepository.save(insumo);
    }
}

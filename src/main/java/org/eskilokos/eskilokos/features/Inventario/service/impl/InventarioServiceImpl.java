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
    @Transactional
    public Inventario actualizarStock(Integer id, int cantidadAjustada) {
        Inventario insumo = findInsumoById(id);
        int nuevaCantidad = insumo.getCantidad()+cantidadAjustada;

        if (nuevaCantidad <0){
            throw new RuntimeException("El Stock No Debe Ser Negativo");
        }
        insumo.setCantidad(nuevaCantidad);
        return inventarioRepository.save(insumo);
    }

    @Override
    public void eliminarInsumo(Integer id) {
        if (!inventarioRepository.existsById(id)){
            throw new RuntimeException("No existe el Insumo");
        }
        inventarioRepository.deleteById(id);
    }
}

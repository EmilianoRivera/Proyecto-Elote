package org.eskilokos.eskilokos.features.Inventario.service;

import org.eskilokos.eskilokos.core.entidades.Inventario;

import java.util.List;

public interface InventarioService {
    public List<Inventario> listaTodosLosInsumos(); //findAll
    public Inventario findInsumoById(Integer id);
    public Inventario registrarInsumo(Inventario insumo);
    public Inventario actualizarStock(Integer id, int cantidadAjustada);
    public void eliminarInsumo(Integer id);
}

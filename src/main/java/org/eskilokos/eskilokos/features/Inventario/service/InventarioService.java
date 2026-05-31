package org.eskilokos.eskilokos.features.Inventario.service;

import org.eskilokos.eskilokos.core.entidades.Inventario;

import java.util.List;

public interface InventarioService {
    public List<Inventario> listaTodosLosInsumos(); //findAll
    public Inventario findInsumoById(Integer id);
    public Inventario registrarInsumo(Inventario insumo);
    public void eliminarInsumo(Integer id);

    Inventario agregarStock(Integer id, int cantidad);
    Inventario descontarStock(Integer id, int cantidad);
    Inventario fijarStockManual(Integer id, int cantidadExacta);
}

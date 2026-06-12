package org.eskilokos.eskilokos.features.Inventario.service;

import org.eskilokos.eskilokos.core.entidades.Inventario;

import java.util.List;

public interface InventarioService {
    List<Inventario> listaTodosLosInsumos();

    Inventario findInsumoById(Integer id);

    List<Inventario> buscarInsumosPorNombre(String nombre); // Nuevo contrato añadido

    Inventario registrarInsumo(Inventario insumo);

    Inventario actualizarStock(Integer id, int cantidadAjustada);

    Inventario actualizarInsumoCompleto(Integer id, Inventario insumo); // Sorteamos soporte para PUT completo

    void eliminarInsumo(Integer id);
}

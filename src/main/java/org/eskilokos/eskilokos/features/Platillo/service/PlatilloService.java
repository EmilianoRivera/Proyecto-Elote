package org.eskilokos.eskilokos.features.Platillo.service;

import org.eskilokos.eskilokos.core.entidades.Platillo;
import org.eskilokos.eskilokos.core.entidades.PlatilloInsumo;

import java.util.List;
import java.util.Optional;

public interface PlatilloService {
    List<Platillo> findAll();
    Optional<Platillo> findById(Integer id);
    Platillo save(Platillo platillo);
    Platillo update(Integer id, Platillo platillo);
    void deleteById(Integer id);

    List<Platillo> findByNombre(String nombre);

    // Inventario (Necesitar): insumos que necesita un platillo
    List<PlatilloInsumo> getInsumos(Integer idPlatillo);
    Platillo agregarInsumo(Integer idPlatillo, Integer idInsumo, Integer cantidadNecesaria);
    Platillo quitarInsumo(Integer idPlatillo, Integer idInsumo);
}

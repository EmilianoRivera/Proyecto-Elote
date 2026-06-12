package org.eskilokos.eskilokos.features.Platillo.service.impl;

import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.eskilokos.eskilokos.core.entidades.Platillo;
import org.eskilokos.eskilokos.core.entidades.PlatilloInsumo;
import org.eskilokos.eskilokos.features.Platillo.repository.PlatilloRepository;
import org.eskilokos.eskilokos.features.Platillo.service.PlatilloService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlatilloSeviceImpl implements PlatilloService {

    private final PlatilloRepository platilloRepository;

    public PlatilloSeviceImpl(PlatilloRepository platilloRepository) {
        this.platilloRepository = platilloRepository;
    }

    @Override
    public List<Platillo> findAll() {
        return platilloRepository.findAll();
    }

    @Override
    public Optional<Platillo> findById(Integer id) {
        return platilloRepository.findById(id);
    }

    @Override
    public Platillo save(Platillo platillo) {
        return platilloRepository.save(platillo);
    }

    @Override
    public Platillo update(Integer id, Platillo platillo) {
        Platillo existing = platilloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado con id: " + id));
        existing.setNombre(platillo.getNombre());
        existing.setDescripcion(platillo.getDescripcion());
        existing.setPrecio(platillo.getPrecio());
        existing.setTamanos(platillo.getTamanos());
        return platilloRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        platilloRepository.deleteById(id);
    }


    @Override
    public List<Platillo> findByNombre(String nombre) {
        return platilloRepository.findByNombreContainingIgnoreCase(nombre);
    }


    // --- Inventario (Necesitar) ---
    @Override
    public List<PlatilloInsumo> getInsumos(Integer idPlatillo) {
        Platillo platillo = platilloRepository.findById(idPlatillo)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado con id: " + idPlatillo));
        return new ArrayList<>(platillo.getIngredientes());
    }

    @Override
    public Platillo agregarInsumo(Integer idPlatillo, Integer idInsumo, Integer cantidadNecesaria) {
        Platillo platillo = platilloRepository.findById(idPlatillo)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado con id: " + idPlatillo));

        // Si ya existe ese insumo, actualiza la cantidad necesaria
        Optional<PlatilloInsumo> existente = platillo.getIngredientes().stream()
                .filter(i -> i.getInsumo().getIdInsumo().equals(idInsumo))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidadNecesaria(cantidadNecesaria);
        } else {
            PlatilloInsumo nuevo = new PlatilloInsumo(platillo, new Inventario(idInsumo), cantidadNecesaria);
            platillo.getIngredientes().add(nuevo);
        }

        return platilloRepository.save(platillo);
    }

    @Override
    public Platillo quitarInsumo(Integer idPlatillo, Integer idInsumo) {
        Platillo platillo = platilloRepository.findById(idPlatillo)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado con id: " + idPlatillo));

        platillo.getIngredientes().removeIf(i -> i.getInsumo().getIdInsumo().equals(idInsumo));
        return platilloRepository.save(platillo);
    }
}

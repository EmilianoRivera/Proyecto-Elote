package org.eskilokos.eskilokos.features.Platillo.controller;

import org.eskilokos.eskilokos.core.entidades.Platillo;
import org.eskilokos.eskilokos.core.entidades.PlatilloInsumo;
import org.eskilokos.eskilokos.features.Platillo.service.PlatilloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/platillos")
public class PlatilloController {

    private final PlatilloService platilloService;

    public PlatilloController(PlatilloService platilloService) {
        this.platilloService = platilloService;
    }

    @GetMapping
    public ResponseEntity<List<Platillo>> getAll() {
        return ResponseEntity.ok(platilloService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Platillo> getById(@PathVariable Integer id) {
        return platilloService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Platillo> create(@RequestBody Platillo platillo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platilloService.save(platillo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Platillo> update(@PathVariable Integer id, @RequestBody Platillo platillo) {
        return ResponseEntity.ok(platilloService.update(id, platillo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        platilloService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Inventario (Necesitar): ver insumos de un platillo ---
    @GetMapping("/{id}/insumos")
    public ResponseEntity<List<PlatilloInsumo>> getInsumos(@PathVariable Integer id) {
        return ResponseEntity.ok(platilloService.getInsumos(id));
    }

    // --- Inventario (Necesitar): agregar o actualizar insumo en platillo ---
    // Body esperado: { "idInsumo": 1, "cantidadNecesaria": 3 }
    @PostMapping("/{id}/insumos")
    public ResponseEntity<Platillo> agregarInsumo(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(
                platilloService.agregarInsumo(id, body.get("idInsumo"), body.get("cantidadNecesaria"))
        );
    }

    // --- Inventario (Necesitar): quitar insumo de un platillo ---
    @DeleteMapping("/{id}/insumos/{idInsumo}")
    public ResponseEntity<Platillo> quitarInsumo(
            @PathVariable Integer id,
            @PathVariable Integer idInsumo) {
        return ResponseEntity.ok(platilloService.quitarInsumo(id, idInsumo));
    }
}

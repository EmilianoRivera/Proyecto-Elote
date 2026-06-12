package org.eskilokos.eskilokos.features.Platillo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Platillo;
import org.eskilokos.eskilokos.core.entidades.PlatilloInsumo;
import org.eskilokos.eskilokos.features.Platillo.service.PlatilloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Platillos", description = "Gestión de platillos del menú")
@RequestMapping("/api/v1/platillos")
public class PlatilloController {

    private final PlatilloService platilloService;

    public PlatilloController(PlatilloService platilloService) {
        this.platilloService = platilloService;
    }

    @GetMapping
    @Operation(summary = "Visualizar todo el Menu")
    public ResponseEntity<List<Platillo>> getAll() {
        return ResponseEntity.ok(platilloService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Visualizar por ID", description = "Visualiza los registros buscando por ID")
    public ResponseEntity<Platillo> getById(@PathVariable Integer id) {
        return platilloService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar platillos por nombre", description = "Busca platillos que contengan el texto enviado")
    public ResponseEntity<List<Platillo>> getByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(platilloService.findByNombre(nombre));
    }
    @PostMapping
    @Operation(summary = "Dar de Alta un nuevo Platillo")
    public ResponseEntity<Platillo> create(@RequestBody Platillo platillo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platilloService.save(platillo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar Platillo buscado por ID")
    public ResponseEntity<Platillo> update(@PathVariable Integer id, @RequestBody Platillo platillo) {
        return ResponseEntity.ok(platilloService.update(id, platillo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Platillo por ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        platilloService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Inventario (Necesitar): ver insumos de un platillo ---
    @GetMapping("/{id}/insumos")
    @Operation(summary = "Obtener los insumos de un platillo")
    public ResponseEntity<List<PlatilloInsumo>> getInsumos(@PathVariable Integer id) {
        List<PlatilloInsumo> insumos = platilloService.getInsumos(id);
        if (insumos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(insumos);
    }

    // --- Inventario (Necesitar): agregar o actualizar insumo en platillo ---
    @PostMapping("/{id}/insumos")
    @Operation(summary = "Agregar o actualizar insumo en un platillo")
    public ResponseEntity<List<PlatilloInsumo>> agregarInsumo(
            @PathVariable Integer id,
            @RequestBody org.eskilokos.eskilokos.features.Platillo.DTOs.PlatilloInsumoRequestDTO body) {

        // Validación preventiva usando tu DTO existente
        if (body.getIdInsumo() == null || body.getCantidadNecesaria() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Ejecutamos la acción en el Service
        platilloService.agregarInsumo(
                id,
                body.getIdInsumo(),
                body.getCantidadNecesaria()
        );

        // En lugar de retornar el objeto Platillo roto, reutilizamos el GET para devolver la lista actualizada
        return getInsumos(id);
    }

    // --- Inventario (Necesitar): quitar insumo de un platillo ---
    @DeleteMapping("/{id}/insumos/{idInsumo}")
    @Operation(summary = "Quitar un insumo específico de un platillo")
    public ResponseEntity<List<PlatilloInsumo>> quitarInsumo(
            @PathVariable Integer id,
            @PathVariable Integer idInsumo) {

        // Ejecutamos la eliminación en el Service
        platilloService.quitarInsumo(id, idInsumo);

        // Retornamos la lista de insumos actualizada tras la eliminación
        return getInsumos(id);
    }
}

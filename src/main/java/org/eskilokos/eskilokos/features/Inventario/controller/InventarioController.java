package org.eskilokos.eskilokos.features.Inventario.controller;

import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.eskilokos.eskilokos.features.Inventario.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    // Inyección de dependencias
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // GET: /api/v1/inventario
    @GetMapping
    public ResponseEntity<List<Inventario>> listarInsumos() {
        return ResponseEntity.ok(inventarioService.listaTodosLosInsumos());
    }

    // GET: /api/v1/inventario/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerInsumo(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.findInsumoById(id));
    }

    // POST: /api/v1/inventario
    @PostMapping
    public ResponseEntity<Inventario> registrarInsumo(@RequestBody Inventario insumo) {
        Inventario nuevoInsumo = inventarioService.registrarInsumo(insumo);
        return new ResponseEntity<>(nuevoInsumo, HttpStatus.CREATED);
    }

    // PATCH: /api/v1/inventario/{id}/ajustar?cantidad=5
    @PatchMapping("/{id}/ajustar")
    public ResponseEntity<Inventario> ajustarStock(@PathVariable Integer id, @RequestParam int cantidad) {
        Inventario insumoActualizado = inventarioService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(insumoActualizado);
    }

    // DELETE: /api/v1/inventario/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Integer id) {
        inventarioService.eliminarInsumo(id);
        return ResponseEntity.noContent().build();
    }
}
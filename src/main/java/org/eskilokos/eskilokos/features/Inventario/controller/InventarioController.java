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

    @PatchMapping("/{id}/agregar")
    public ResponseEntity<Inventario> agregarStock(@PathVariable Integer id, @RequestParam int cantidad) {
        return ResponseEntity.ok(inventarioService.agregarStock(id, cantidad));
    }

    @PatchMapping("/{id}/descontar")
    public ResponseEntity<Inventario> descontarStock(@PathVariable Integer id, @RequestParam int cantidad) {
        return ResponseEntity.ok(inventarioService.descontarStock(id, cantidad));
    }

    @PutMapping("/{id}/corregir")
    public ResponseEntity<Inventario> corregirStock(@PathVariable Integer id, @RequestParam int cantidadExacta) {
        return ResponseEntity.ok(inventarioService.fijarStockManual(id, cantidadExacta));
    }

    // DELETE: /api/v1/inventario/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Integer id) {
        inventarioService.eliminarInsumo(id);
        return ResponseEntity.noContent().build();
    }
}
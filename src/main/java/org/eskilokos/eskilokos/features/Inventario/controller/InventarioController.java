package org.eskilokos.eskilokos.features.Inventario.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Inventario;
import org.eskilokos.eskilokos.features.Inventario.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventario", description = "Endpoints para el control de stock, insumos e ingredientes del restaurante")
@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @Operation(
            summary = "Listar u obtener insumos",
            description = "Recupera la lista de todos los insumos disponibles. Si se pasa el parámetro opcional 'nombre', filtra las coincidencias."
    )
    public ResponseEntity<List<Inventario>> listarInsumos(@RequestParam(required = false) String nombre) {
        List<Inventario> resultado = (nombre != null && !nombre.isBlank())
                ? inventarioService.buscarInsumosPorNombre(nombre)
                : inventarioService.listaTodosLosInsumos();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un insumo por ID",
            description = "Busca los datos y stock actual de un insumo utilizando su identificador único."
    )
    public ResponseEntity<Inventario> obtenerInsumo(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.findInsumoById(id));
    }

    @PostMapping
    @Operation(
            summary = "Registrar nuevo insumo",
            description = "Da de alta un ingrediente o artículo nuevo en el inventario base."
    )
    public ResponseEntity<Inventario> registrarInsumo(@RequestBody Inventario insumo) {
        Inventario nuevoInsumo = inventarioService.registrarInsumo(insumo);
        return new ResponseEntity<>(nuevoInsumo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modificar un insumo por completo",
            description = "Reemplaza o edita por completo los campos (nombre, caducidad, stock bruto) de un registro existente."
    )
    public ResponseEntity<Inventario> editarInsumoCompleto(@PathVariable Integer id, @RequestBody Inventario insumo) {
        return ResponseEntity.ok(inventarioService.actualizarInsumoCompleto(id, insumo));
    }

    @PatchMapping("/{id}/ajustar")
    @Operation(
            summary = "Ajustar stock incremental/decremental",
            description = "Suma o resta unidades directamente al stock actual. Envía valores positivos para agregar y negativos para mermas/consumos (ej. cantidad=-5)."
    )
    public ResponseEntity<Inventario> ajustarStock(@PathVariable Integer id, @RequestParam int cantidad) {
        Inventario insumoActualizado = inventarioService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(insumoActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar insumo del inventario",
            description = "Borra definitivamente el registro de un insumo basándose en su ID único."
    )
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Integer id) {
        inventarioService.eliminarInsumo(id);
        return ResponseEntity.noContent().build();
    }
}
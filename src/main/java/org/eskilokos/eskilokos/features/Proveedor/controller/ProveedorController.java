package org.eskilokos.eskilokos.features.Proveedor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.eskilokos.eskilokos.core.entidades.Proveedor;
import org.eskilokos.eskilokos.features.Proveedor.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Proveedores", description = "Endpoints para la gestión de las empresas y distribuidores que surten los insumos del inventario")
@RestController
@RequestMapping("/api/v1/proveedores") // Ajustado a v1 para mantener tu estándar
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @Operation(
            summary = "Obtener o buscar proveedores",
            description = "Devuelve la lista completa de proveedores. Si se proporciona el parámetro opcional 'nombre', filtra los registros que coincidan con dicho texto."
    )
    public ResponseEntity<List<Proveedor>> obtenerTodos(
            @RequestParam(required = false) String nombre) {
        List<Proveedor> resultado = (nombre != null && !nombre.isBlank())
                ? proveedorService.buscarPorNombre(nombre)
                : proveedorService.obtenerTodos();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener proveedor por ID",
            description = "Busca en la base de datos y devuelve la información de un proveedor específico utilizando su ID único."
    )
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer id) {
        return proveedorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo proveedor",
            description = "Registra un proveedor en el sistema (por ejemplo: distribuidores de elotes, lácteos, etc.) utilizando los datos enviados en el cuerpo de la petición."
    )
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.guardar(proveedor));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un proveedor existente",
            description = "Modifica los datos de contacto o fiscales de un proveedor específico buscando por su identificador único."
    )
    public ResponseEntity<Proveedor> actualizar(
            @PathVariable Integer id,
            @RequestBody Proveedor proveedor) {
        try {
            return ResponseEntity.ok(proveedorService.actualizar(id, proveedor));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un proveedor por ID",
            description = "Remueve de manera permanente el registro de un proveedor del sistema mediante su ID único."
    )
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
package org.eskilokos.eskilokos.features.Personal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.eskilokos.eskilokos.features.Personal.DTOs.CocineroRequestDTO;
import org.eskilokos.eskilokos.features.Personal.DTOs.RepartidorRequestDTO;
import org.eskilokos.eskilokos.features.Personal.service.PersonalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Personal", description = "Endpoints para la gestión total del personal operativo (Cocineros y Repartidores)")
@RestController
@RequestMapping("/api/v1/personal")
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    // ==========================================
    // --- 🧑‍🍳 SECCIÓN DE COCINEROS (CRUD COMPLETO) ---
    // ==========================================

    @GetMapping("/cocineros")
    @Operation(
            summary = "Obtener todos los cocineros",
            description = "Recupera una lista completa con todos los registros de cocineros dados de alta."
    )
    public ResponseEntity<List<Cocinero>> getCocineros() {
        return ResponseEntity.ok(personalService.listarCocineros());
    }

    @GetMapping("/cocineros/{id}")
    @Operation(
            summary = "Obtener cocinero por ID",
            description = "Busca y devuelve la información de un cocinero específico a través de su identificador único."
    )
    public ResponseEntity<Cocinero> getCocineroPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(personalService.obtenerCocineroPorId(id));
    }

    @GetMapping("/cocineros/buscar")
    @Operation(
            summary = "Buscar cocineros por nombre",
            description = "Filtra y devuelve una lista de cocineros cuyos nombres coincidan o contengan el texto enviado en los parámetros."
    )
    public ResponseEntity<List<Cocinero>> buscarCocinerosPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(personalService.buscarCocinerosPorNombre(nombre));
    }

    @PostMapping("/cocineros")
    @Operation(
            summary = "Dar de alta un nuevo cocinero",
            description = "Registra un nuevo cocinero en el sistema utilizando los datos del DTO de entrada."
    )
    public ResponseEntity<Cocinero> registrarCocinero(@RequestBody CocineroRequestDTO dto) {
        return new ResponseEntity<>(personalService.registrarCocinero(dto), HttpStatus.CREATED);
    }

    @PutMapping("/cocineros/{id}")
    @Operation(
            summary = "Editar datos de un cocinero",
            description = "Actualiza la información (como datos de empleado asociados) de un cocinero existente buscando por su ID."
    )
    public ResponseEntity<Cocinero> editarCocinero(@PathVariable Integer id, @RequestBody CocineroRequestDTO dto) {
        return ResponseEntity.ok(personalService.actualizarCocinero(id, dto));
    }

    @DeleteMapping("/cocineros/{id}")
    @Operation(
            summary = "Eliminar un cocinero",
            description = "Remueve de forma permanente el registro de un cocinero del sistema mediante su ID."
    )
    public ResponseEntity<Void> eliminarCocinero(@PathVariable Integer id) {
        personalService.eliminarCocinero(id);
        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // --- 🛵 SECCIÓN DE REPARTIDORES (CRUD COMPLETO) ---
    // ==========================================

    @GetMapping("/repartidores")
    @Operation(
            summary = "Obtener todos los repartidores",
            description = "Recupera una lista completa con todos los repartidores registrados en el sistema."
    )
    public ResponseEntity<List<Repartidor>> getRepartidores() {
        return ResponseEntity.ok(personalService.listarRepartidores());
    }

    @GetMapping("/repartidores/{id}")
    @Operation(
            summary = "Obtener repartidor por ID",
            description = "Busca y devuelve los datos de un repartidor específico utilizando su identificador único."
    )
    public ResponseEntity<Repartidor> getRepartidorPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(personalService.obtenerRepartidorPorId(id));
    }

    @GetMapping("/repartidores/buscar")
    @Operation(
            summary = "Buscar repartidores por nombre",
            description = "Filtra y devuelve una lista de repartidores cuyos nombres coincidan con el parámetro de búsqueda enviado."
    )
    public ResponseEntity<List<Repartidor>> buscarRepartidoresPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(personalService.buscarRepartidoresPorNombre(nombre));
    }

    @GetMapping("/repartidores/disponibles")
    @Operation(
            summary = "Listar repartidores disponibles",
            description = "Muestra únicamente a los repartidores que están activos y listos para recibir y entregar pedidos en ruta."
    )
    public ResponseEntity<List<Repartidor>> getRepartidoresDisponibles() {
        return ResponseEntity.ok(personalService.listarRepartidoresDisponibles());
    }

    @PostMapping("/repartidores")
    @Operation(
            summary = "Dar de alta un nuevo repartidor",
            description = "Registra un nuevo repartidor en la base de datos utilizando los campos obligatorios de su DTO."
    )
    public ResponseEntity<Repartidor> registrarRepartidor(@RequestBody RepartidorRequestDTO dto) {
        return new ResponseEntity<>(personalService.registrarRepartidor(dto), HttpStatus.CREATED);
    }

    @PutMapping("/repartidores/{id}")
    @Operation(
            summary = "Editar datos de un repartidor",
            description = "Modifica la información registrada de un repartidor específico (como estatus o vehículo asignado) basándose en su ID."
    )
    public ResponseEntity<Repartidor> editarRepartidor(@PathVariable Integer id, @RequestBody RepartidorRequestDTO dto) {
        return ResponseEntity.ok(personalService.actualizarRepartidor(id, dto));
    }

    @DeleteMapping("/repartidores/{id}")
    @Operation(
            summary = "Eliminar un repartidor",
            description = "Borra definitivamente el registro de un repartidor del sistema mediante su identificador único."
    )
    public ResponseEntity<Void> eliminarRepartidor(@PathVariable Integer id) {
        personalService.eliminarRepartidor(id);
        return ResponseEntity.noContent().build();
    }
}
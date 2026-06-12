package org.eskilokos.eskilokos.features.Pedido.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Pedidos", description = "Gestión de pedidos")
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public ResponseEntity<List<Pedido>> getAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID UNICO DE PEDIDO")
    public ResponseEntity<Pedido> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Registra una nueva orden en el sistema. Por defecto, el backend forzará los estados a 'Recibido' / 'RECIBIDO' y gatillará la primera plantilla de correo electrónico."
    )
    public ResponseEntity<Pedido> create(
            @RequestBody org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Avanzar fase o actualizar el pedido",
            description = "Permite modificar los datos del pedido. Si el campo 'estadoReparto' cambia (ej. de PREPARACION a EN_REPARTO), el sistema disparará automáticamente el correo HTML prediseñado para esa fase."
    )
    public ResponseEntity<Pedido> update(
            @PathVariable Integer id,
            @RequestBody org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO dto) {
        return ResponseEntity.ok(pedidoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido por ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Cliente (Hacer): pedidos de un cliente específico ---
    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Buscar Todos los pedidos del Cliente")
    public ResponseEntity<List<Pedido>> getByCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pedidoService.findByClienteId(idCliente));
    }

    // --- Contenido (Contener): ver platillos de un pedido ---
    @GetMapping("/{id}/contenido")
    @Operation(summary = "Ver los plastillos de un pedido por ID Pedido")
    public ResponseEntity<List<PedidoContenido>> getContenido(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }

    // --- Contenido (Contener): agregar lista de platillos en un pedido ---
    @PostMapping("/{id}/contenido")
    @Operation(summary = "Agregar o actualizar múltiples platillos en un pedido a la vez")
    public ResponseEntity<List<org.eskilokos.eskilokos.core.entidades.PedidoContenido>> agregarPlatillos(
            @PathVariable Integer id,
            @RequestBody List<org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoRequestDTO> bodyList) {

        // Validamos de forma preventiva que la lista no venga vacía
        if (bodyList == null || bodyList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Mandamos a procesar cada platillo de la lista al Service
        for (org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoRequestDTO item : bodyList) {
            pedidoService.agregarPlatillo(id, item.getIdPlatillo(), item.getCantidad());
        }

        // Retornamos la lista de lo que contiene el pedido de forma limpia y plana
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }

    // --- Contenido (Contener): quitar platillo de un pedido ---
    @DeleteMapping("/{id}/contenido/{idPlatillo}")
    @Operation(summary = "Eliminar Todo un TIPO DE Platillo")
    public ResponseEntity<Pedido> quitarPlatillo(
            @PathVariable Integer id,
            @PathVariable Integer idPlatillo) {
        return ResponseEntity.ok(pedidoService.quitarPlatillo(id, idPlatillo));
    }

    // --- Contenido (Contener): modificar la cantidad de un platillo en un pedido ---
    @PutMapping("/{id}/contenido/{idPlatillo}")
    @Operation(summary = "Modificar la cantidad de un platillo específico dentro de un pedido")
    public ResponseEntity<List<org.eskilokos.eskilokos.core.entidades.PedidoContenido>> actualizarCantidadPlatillo(
            @PathVariable Integer id,
            @PathVariable Integer idPlatillo,
            @RequestBody org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO dto) {

        // Validación preventiva
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            return ResponseEntity.badRequest().build(); // Si mandan 0 o negativo, es inválido
        }

        // Reutilizamos la lógica del Service que ya sabe actualizar cantidades
        pedidoService.agregarPlatillo(id, idPlatillo, dto.getCantidad());

        // Devolvemos la lista de contenido actualizada de forma plana (sin errores 500)
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }
}

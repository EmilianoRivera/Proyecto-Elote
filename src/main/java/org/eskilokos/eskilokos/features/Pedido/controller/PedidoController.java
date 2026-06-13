package org.eskilokos.eskilokos.features.Pedido.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoUpdateDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "Obtener pedido por ID único")
    public ResponseEntity<Pedido> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    public ResponseEntity<Pedido> create(@RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "(Estatus de reparto)  ESPERA_REPARTIDOR PREPARACION EN_REPARTO ENTREGADO")
    public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody PedidoUpdateDTO dto) {
        return ResponseEntity.ok(pedidoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido por ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Buscar todos los pedidos de un Cliente")
    public ResponseEntity<List<Pedido>> getByCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pedidoService.findByClienteId(idCliente));
    }

    @GetMapping("/{id}/contenido")
    @Operation(summary = "Ver los platillos de un pedido por ID de Pedido")
    public ResponseEntity<List<PedidoContenido>> getContenido(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }

    @PostMapping("/{id}/contenido")
    @Operation(summary = "Agregar múltiples platillos a un pedido a la vez")
    public ResponseEntity<List<PedidoContenido>> agregarPlatillos(
            @PathVariable Integer id,
            @RequestBody List<PedidoContenidoRequestDTO> bodyList) {

        if (bodyList == null || bodyList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        for (PedidoContenidoRequestDTO item : bodyList) {
            pedidoService.agregarPlatillo(id, item.getIdPlatillo(), item.getCantidad());
        }

        return ResponseEntity.ok(pedidoService.getContenido(id));
    }

    @DeleteMapping("/{id}/contenido/{idPlatillo}")
    @Operation(summary = "Eliminar todo un tipo de platillo de la orden")
    public ResponseEntity<Pedido> quitarPlatillo(@PathVariable Integer id, @PathVariable Integer idPlatillo) {
        return ResponseEntity.ok(pedidoService.quitarPlatillo(id, idPlatillo));
    }

    @PutMapping("/{id}/contenido/{idPlatillo}")
    @Operation(summary = "Modificar la cantidad de un platillo específico recalculando costos")
    public ResponseEntity<List<PedidoContenido>> actualizarCantidadPlatillo(
            @PathVariable Integer id,
            @PathVariable Integer idPlatillo,
            @RequestBody PedidoContenidoUpdateDTO dto) {

        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        pedidoService.agregarPlatillo(id, idPlatillo, dto.getCantidad());
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }
}
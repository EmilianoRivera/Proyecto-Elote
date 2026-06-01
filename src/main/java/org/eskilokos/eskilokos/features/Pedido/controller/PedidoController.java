package org.eskilokos.eskilokos.features.Pedido.controller;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> create(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.update(id, pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Cliente (Hacer): pedidos de un cliente específico ---
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> getByCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pedidoService.findByClienteId(idCliente));
    }

    // --- Contenido (Contener): ver platillos de un pedido ---
    @GetMapping("/{id}/contenido")
    public ResponseEntity<List<PedidoContenido>> getContenido(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.getContenido(id));
    }

    // --- Contenido (Contener): agregar o actualizar platillo en pedido ---
    // Body esperado: { "idPlatillo": 1, "cantidad": 2 }
    @PostMapping("/{id}/contenido")
    public ResponseEntity<Pedido> agregarPlatillo(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(
                pedidoService.agregarPlatillo(id, body.get("idPlatillo"), body.get("cantidad"))
        );
    }

    // --- Contenido (Contener): quitar platillo de un pedido ---
    @DeleteMapping("/{id}/contenido/{idPlatillo}")
    public ResponseEntity<Pedido> quitarPlatillo(
            @PathVariable Integer id,
            @PathVariable Integer idPlatillo) {
        return ResponseEntity.ok(pedidoService.quitarPlatillo(id, idPlatillo));
    }
}

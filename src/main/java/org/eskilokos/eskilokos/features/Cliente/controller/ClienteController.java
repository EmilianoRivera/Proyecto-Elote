package org.eskilokos.eskilokos.features.Cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eskilokos.eskilokos.core.entidades.Cliente;
import org.eskilokos.eskilokos.features.Cliente.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Endpoints para la gestión, registro y base de datos de los comensales")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    // Inyección limpia por constructor (Se eliminó el @Autowired redundante)
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(
            summary = "Listar u obtener clientes",
            description = "Devuelve todos los clientes del restaurante. Si se proporciona el parámetro opcional 'nombre', filtra los resultados coincidentes."
    )
    public ResponseEntity<List<Cliente>> listarClientes(@RequestParam(required = false) String nombre) {
        List<Cliente> resultado = (nombre != null && !nombre.isBlank())
                ? clienteService.buscarPorNombre(nombre)
                : clienteService.findAll();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Recupera el perfil, teléfono y correo electrónico de un cliente basándose en su ID único."
    )
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @PostMapping
    @Operation(
            summary = "Dar de alta un nuevo cliente",
            description = "Registra un cliente en el sistema para permitirle levantar órdenes y recibir notificaciones por correo."
    )
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.save(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar datos de un cliente",
            description = "Modifica los datos personales (nombre, teléfono o email) de un cliente existente localizándolo por su ID."
    )
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un cliente",
            description = "Borra permanentemente el registro de un cliente de la base de datos mediante su ID único."
    )
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
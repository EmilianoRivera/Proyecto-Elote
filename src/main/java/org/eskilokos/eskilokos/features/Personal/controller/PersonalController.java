package org.eskilokos.eskilokos.features.Personal.controller;

import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.eskilokos.eskilokos.features.Personal.DTOs.CocineroRequestDTO;
import org.eskilokos.eskilokos.features.Personal.DTOs.RepartidorRequestDTO;
import org.eskilokos.eskilokos.features.Personal.service.PersonalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personal")
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    // --- Endpoints de Cocineros ---
    @GetMapping("/cocineros")
    public ResponseEntity<List<Cocinero>> getCocineros() {
        return ResponseEntity.ok(personalService.listarCocineros());
    }

    @PostMapping("/cocineros")
    public ResponseEntity<Cocinero> registrarCocinero(@RequestBody CocineroRequestDTO dto) {
        return new ResponseEntity<>(personalService.registrarCocinero(dto), HttpStatus.CREATED);
    }

    // --- Endpoints de Repartidores ---
    @GetMapping("/repartidores")
    public ResponseEntity<List<Repartidor>> getRepartidores() {
        return ResponseEntity.ok(personalService.listarRepartidores());
    }

    @GetMapping("/repartidores/disponibles")
    public ResponseEntity<List<Repartidor>> getRepartidoresDisponibles() {
        return ResponseEntity.ok(personalService.listarRepartidoresDisponibles());
    }

    @PostMapping("/repartidores")
    public ResponseEntity<Repartidor> registrarRepartidor(@RequestBody RepartidorRequestDTO dto) {
        return new ResponseEntity<>(personalService.registrarRepartidor(dto), HttpStatus.CREATED);
    }
}
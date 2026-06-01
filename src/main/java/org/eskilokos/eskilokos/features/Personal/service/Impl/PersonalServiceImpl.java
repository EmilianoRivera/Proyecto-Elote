package org.eskilokos.eskilokos.features.Personal.service.Impl;

import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.eskilokos.eskilokos.core.entidades.Empleado;
import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.eskilokos.eskilokos.features.Personal.DTOs.CocineroRequestDTO;
import org.eskilokos.eskilokos.features.Personal.DTOs.RepartidorRequestDTO;
import org.eskilokos.eskilokos.features.Personal.repository.CocineroRepository;
import org.eskilokos.eskilokos.features.Personal.repository.EmpleadoRepository;
import org.eskilokos.eskilokos.features.Personal.repository.RepartidorRepository;
import org.eskilokos.eskilokos.features.Personal.service.PersonalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {

    private final EmpleadoRepository empleadoRepository;
    private final CocineroRepository cocineroRepository;
    private final RepartidorRepository repartidorRepository;

    public PersonalServiceImpl(EmpleadoRepository empleadoRepository,
                               CocineroRepository cocineroRepository,
                               RepartidorRepository repartidorRepository) {
        this.empleadoRepository = empleadoRepository;
        this.cocineroRepository = cocineroRepository;
        this.repartidorRepository = repartidorRepository;
    }

    // ==========================================
    // COCINEROS
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<Cocinero> listarCocineros() {
        return cocineroRepository.findAll();
    }

    @Override
    @Transactional
    public Cocinero registrarCocinero(CocineroRequestDTO dto) {
        // 1. Crear o buscar al Empleado
        Empleado empleado = empleadoRepository.findById(dto.rfc()).orElseGet(() -> {
            Empleado nuevoEmpleado = new Empleado(dto.rfc());
            nuevoEmpleado.setNombre(dto.nombre());
            nuevoEmpleado.setEmail(dto.email());
            nuevoEmpleado.setTelefono(dto.telefono());
            return empleadoRepository.save(nuevoEmpleado);
        });

        // 2. Crear al Cocinero y asociarlo
        Cocinero cocinero = new Cocinero();
        cocinero.setEmpleado(empleado);
        cocinero.setNumCocineros(dto.numCocineros());

        return cocineroRepository.save(cocinero);
    }

    // ==========================================
    // REPARTIDORES
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidores() {
        return repartidorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidoresDisponibles() {
        // Devuelve a los que tienen al menos 1 entrega disponible
        return repartidorRepository.findByEntregasDispGreaterThan(0);
    }

    @Override
    @Transactional
    public Repartidor registrarRepartidor(RepartidorRequestDTO dto) {
        // 1. Crear o buscar al Empleado
        Empleado empleado = empleadoRepository.findById(dto.rfc()).orElseGet(() -> {
            Empleado nuevoEmpleado = new Empleado(dto.rfc());
            nuevoEmpleado.setNombre(dto.nombre());
            nuevoEmpleado.setEmail(dto.email());
            nuevoEmpleado.setTelefono(dto.telefono());
            return empleadoRepository.save(nuevoEmpleado);
        });

        // 2. Crear al Repartidor y asociarlo
        Repartidor repartidor = new Repartidor();
        repartidor.setEmpleado(empleado);
        repartidor.setEntregasDisp(dto.entregasDisp());
        repartidor.setNumRepartidor(dto.numRepartidores());

        return repartidorRepository.save(repartidor);
    }
}
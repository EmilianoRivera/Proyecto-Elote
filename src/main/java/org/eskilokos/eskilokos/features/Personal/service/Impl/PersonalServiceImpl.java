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
    // --- 🧑‍🍳 IMPLEMENTACIÓN COCINEROS ---
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<Cocinero> listarCocineros() {
        return cocineroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cocinero obtenerCocineroPorId(Integer id) {
        return cocineroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cocinero no encontrado con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cocinero> buscarCocinerosPorNombre(String nombre) {
        return cocineroRepository.findByEmpleado_NombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Cocinero registrarCocinero(CocineroRequestDTO dto) {
        Empleado empleado = empleadoRepository.findById(dto.rfc()).orElseGet(() -> {
            Empleado nuevoEmpleado = new Empleado(dto.rfc());
            nuevoEmpleado.setNombre(dto.nombre());
            nuevoEmpleado.setEmail(dto.email());
            nuevoEmpleado.setTelefono(dto.telefono());
            return empleadoRepository.save(nuevoEmpleado);
        });

        Cocinero cocinero = new Cocinero();
        cocinero.setEmpleado(empleado);
        cocinero.setNumCocineros(dto.numCocineros());

        return cocineroRepository.save(cocinero);
    }

    @Override
    @Transactional
    public Cocinero actualizarCocinero(Integer id, CocineroRequestDTO dto) {
        Cocinero cocineroExistente = obtenerCocineroPorId(id);

        Empleado empleado = cocineroExistente.getEmpleado();
        empleado.setNombre(dto.nombre());
        empleado.setEmail(dto.email());
        empleado.setTelefono(dto.telefono());
        empleadoRepository.save(empleado);

        cocineroExistente.setNumCocineros(dto.numCocineros());

        return cocineroRepository.save(cocineroExistente);
    }

    @Override
    @Transactional
    public void eliminarCocinero(Integer id) {
        Cocinero cocinero = obtenerCocineroPorId(id);
        cocineroRepository.delete(cocinero);
    }

    // ==========================================
    // --- 🛵 IMPLEMENTACIÓN REPARTIDORES ---
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidores() {
        return repartidorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidoresDisponibles() {
        return repartidorRepository.findByEntregasDispGreaterThan(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Repartidor obtenerRepartidorPorId(Integer id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> buscarRepartidoresPorNombre(String nombre) {
        return repartidorRepository.findByEmpleado_NombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional
    public Repartidor registrarRepartidor(RepartidorRequestDTO dto) {
        Empleado empleado = empleadoRepository.findById(dto.rfc()).orElseGet(() -> {
            Empleado nuevoEmpleado = new Empleado(dto.rfc());
            nuevoEmpleado.setNombre(dto.nombre());
            nuevoEmpleado.setEmail(dto.email());
            nuevoEmpleado.setTelefono(dto.telefono());
            return empleadoRepository.save(nuevoEmpleado);
        });

        Repartidor repartidor = new Repartidor();
        repartidor.setEmpleado(empleado);
        repartidor.setEntregasDisp(dto.entregasDisp());
        repartidor.setNumRepartidor(dto.numRepartidores());

        return repartidorRepository.save(repartidor);
    }

    @Override
    @Transactional
    public Repartidor actualizarRepartidor(Integer id, RepartidorRequestDTO dto) {
        Repartidor repartidorExistente = obtenerRepartidorPorId(id);

        Empleado empleado = repartidorExistente.getEmpleado();
        empleado.setNombre(dto.nombre());
        empleado.setEmail(dto.email());
        empleado.setTelefono(dto.telefono());
        empleadoRepository.save(empleado);

        repartidorExistente.setEntregasDisp(dto.entregasDisp());
        repartidorExistente.setNumRepartidor(dto.numRepartidores());

        return repartidorRepository.save(repartidorExistente);
    }

    @Override
    @Transactional
    public void eliminarRepartidor(Integer id) {
        Repartidor repartidor = obtenerRepartidorPorId(id);
        repartidorRepository.delete(repartidor);
    }
}
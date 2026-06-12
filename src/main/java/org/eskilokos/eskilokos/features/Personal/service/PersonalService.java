package org.eskilokos.eskilokos.features.Personal.service;

import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.eskilokos.eskilokos.features.Personal.DTOs.CocineroRequestDTO;
import org.eskilokos.eskilokos.features.Personal.DTOs.RepartidorRequestDTO;

import java.util.List;

public interface PersonalService {
    // --- Cocineros ---
    List<Cocinero> listarCocineros();
    Cocinero obtenerCocineroPorId(Integer id);
    List<Cocinero> buscarCocinerosPorNombre(String nombre);
    Cocinero registrarCocinero(CocineroRequestDTO dto);
    Cocinero actualizarCocinero(Integer id, CocineroRequestDTO dto);
    void eliminarCocinero(Integer id);

    // --- Repartidores ---
    List<Repartidor> listarRepartidores();
    List<Repartidor> listarRepartidoresDisponibles();
    Repartidor obtenerRepartidorPorId(Integer id);
    List<Repartidor> buscarRepartidoresPorNombre(String nombre);
    Repartidor registrarRepartidor(RepartidorRequestDTO dto);
    Repartidor actualizarRepartidor(Integer id, RepartidorRequestDTO dto);
    void eliminarRepartidor(Integer id);
}
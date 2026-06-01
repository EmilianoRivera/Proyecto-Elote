package org.eskilokos.eskilokos.features.Personal.service;

import org.eskilokos.eskilokos.core.entidades.Cocinero;
import org.eskilokos.eskilokos.core.entidades.Repartidor;
import org.eskilokos.eskilokos.features.Personal.DTOs.CocineroRequestDTO;
import org.eskilokos.eskilokos.features.Personal.DTOs.RepartidorRequestDTO;

import java.util.List;

public interface PersonalService {
    // Cocineros
    List<Cocinero> listarCocineros();
    Cocinero registrarCocinero(CocineroRequestDTO dto);

    // Repartidores
    List<Repartidor> listarRepartidores();
    List<Repartidor> listarRepartidoresDisponibles();
    Repartidor registrarRepartidor(RepartidorRequestDTO dto);
}
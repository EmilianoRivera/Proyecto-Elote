package org.eskilokos.eskilokos.features.Personal.DTOs;

public record RepartidorRequestDTO(
        String rfc,
        String nombre,
        String email,
        String telefono,
        Integer entregasDisp,
        Integer numRepartidores
) {}
package org.eskilokos.eskilokos.features.Personal.DTOs;

public record CocineroRequestDTO(
        String rfc,
        String nombre,
        String email,
        String telefono,
        Integer numCocineros
) {}
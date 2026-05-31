package org.eskilokos.eskilokos.core.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EncargarId implements Serializable {
    @Column(name = "Cedula_Gerente", length = 20)
    private String cedulaGerente;

    @Column(name = "idProveedor")
    private Integer idProveedor;

    @Column(name = "idInsumo")
    private Integer idInsumo;
}

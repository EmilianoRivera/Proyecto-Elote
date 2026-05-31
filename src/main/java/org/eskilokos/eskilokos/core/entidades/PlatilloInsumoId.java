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
public class PlatilloInsumoId implements Serializable {
    @Column(name = "idPlatillo")
    private Integer idPlatillo;

    @Column(name = "idInsumo")
    private Integer idInsumo;
}

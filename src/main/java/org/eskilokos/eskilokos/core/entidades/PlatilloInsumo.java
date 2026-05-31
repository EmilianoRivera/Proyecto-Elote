package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Platillo_Insumos")
public class PlatilloInsumo implements Serializable {

    @EmbeddedId
    private PlatilloInsumoId id = new PlatilloInsumoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPlatillo")
    @JoinColumn(name = "idPlatillo", referencedColumnName = "idPlatillo")
    @JsonIgnore
    private Platillo platillo;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idInsumo")
    @JoinColumn(name = "idInsumo", referencedColumnName = "idInsumo")
    private Inventario insumo;

    @Column(name = "Cantidad_Necesaria")
    private Integer cantidadNecesaria;

    public PlatilloInsumo(Platillo platillo, Inventario insumo, Integer cantidadNecesaria) {
        this.platillo = platillo;
        this.insumo = insumo;
        this.cantidadNecesaria = cantidadNecesaria;
        this.id.setIdPlatillo(platillo.getIdPlatillo());
        this.id.setIdInsumo(insumo.getIdInsumo());
    }
}

package org.eskilokos.eskilokos.core.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Encargar")
public class Encargar implements Serializable{
    @EmbeddedId
    private EncargarId id = new EncargarId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cedulaGerente")
    @JoinColumn(name = "Cedula_Gerente", referencedColumnName = "Cedula")
    private Gerente gerente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProveedor")
    @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idInsumo")
    @JoinColumn(name = "idInsumo", referencedColumnName = "idInsumo")
    private Inventario insumo;

    public Encargar(Gerente gerente, Proveedor proveedor, Inventario insumo){
        this.gerente = gerente;
        this.proveedor = proveedor;
        this.insumo = insumo;

        this.id.setCedulaGerente(gerente.getCedula());
        this.id.setIdProveedor(proveedor.getIdProveedor());
        this.id.setIdInsumo(insumo.getIdInsumo());
    }
}

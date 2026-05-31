package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Repartidor")
public class Repartidor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRepartidor", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idRepartidor;

    @Column(name = "entregasDisp")
    private Integer entregasDisp;

    @Column(name = "numRepartidor")
    private Integer numRepartidor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RFC_Empleado", referencedColumnName = "RFC", nullable = false)
    private Empleado empleado;

    @JsonIgnore
    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL)
    private Set<Pedido> pedidos = new HashSet<>();

    public Repartidor(Integer idRepartidor){
        this.idRepartidor = idRepartidor;
    }
}

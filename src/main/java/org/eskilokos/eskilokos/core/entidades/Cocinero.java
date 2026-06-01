package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Cocinero")
public class Cocinero implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCocinero", nullable = false)
    @JsonProperty(access =  JsonProperty.Access.READ_ONLY)
    private Integer idCocinero;

    @Column(nullable = false)
    private Integer numCocineros;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RFC_Empleado", referencedColumnName = "RFC", nullable = false)
    private Empleado empleado;

    @JsonIgnore
    @OneToMany(mappedBy = "cocinero", cascade = CascadeType.ALL)
    private Set<Pedido> pedidos = new HashSet<>();

    public Cocinero(Integer idCocinero){
        this.idCocinero = idCocinero;
    }
}

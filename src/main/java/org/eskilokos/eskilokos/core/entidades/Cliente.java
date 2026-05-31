package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Cliente")
public class Cliente implements Serializable {
    // id cliente, nombre, telefono
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idCliente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String telefono;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Set<Pedido> pedidos = new HashSet<>();
    public Cliente(Integer idCliente){
        this.idCliente = idCliente;
    }
}

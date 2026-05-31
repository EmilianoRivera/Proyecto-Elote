package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Inventario")
public class Inventario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "idInsumo", nullable = false)
    private Integer idInsumo;

    @Column(nullable = false, name = "Nombre", length = 100)
    private String nombre;

    @Column(nullable = false, name = "Cantidad")
    @Min(0)
    private Integer cantidad;

    @Column(name = "Caducidad")
    private LocalDate caducidad;

    @JsonIgnore
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL)
    private Set<Encargar> encargos = new HashSet<>();

    public Inventario(Integer idInsumo){
        this.idInsumo = idInsumo;
    }
}

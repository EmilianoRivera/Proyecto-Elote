package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Gerente")
public class Gerente implements Serializable {
    @Id
    @Column(name = "Cedula", length = 20, nullable = false)
    private String cedula;
    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;
    @Column(name = "Email", length = 100)
    private String email;
    @Column(name = "Telefono", length = 20)
    private String telefono;

    @JsonIgnore
    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL)
    private Set<Encargar> encargos = new HashSet<>();

    public Gerente(String cedula){
        this.cedula = cedula;
    }
}

package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Empleado")
public class Empleado implements Serializable{
    @Id
    @Column(name = "RFC", nullable = false, length = 15)
    private String rfc;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @JsonIgnore
    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL)
    private Repartidor repartidor;

    @JsonIgnore
    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL)
    private Cocinero cocinero;

    public Empleado(String rfc){
        this.rfc = rfc;
    }
}

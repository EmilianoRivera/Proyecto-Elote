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
@Table(name = "Proveedor")
public class Proveedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "idProveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "Telefono", length = 20)
    private String telefono;
    @Column(name = "Email", length = 100)
    private String email;
    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @JsonIgnore
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private Set<Encargar> encargos = new HashSet<>();

    public Proveedor(Integer idProveedor){
        this.idProveedor = idProveedor;
    }
}

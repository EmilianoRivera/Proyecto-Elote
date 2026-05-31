package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Platillo")
public class Platillo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "idPlatillo", nullable = false)
    private Integer idPlatillo;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "Precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "Platillo_Tamano",
            joinColumns = @JoinColumn(name = "idPlatillo")
    )
    @Column(name = "Tamano", nullable = false, length = 50)
    private Set<String> tamanos = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "platillo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlatilloInsumo> ingredientes = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "platillo", cascade = CascadeType.ALL)
    private Set<PedidoContenido> pedidos = new HashSet<>();

    public Platillo(Integer idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

}

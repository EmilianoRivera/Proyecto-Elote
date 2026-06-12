package org.eskilokos.eskilokos.features.Platillo.DTOs;
public class PlatilloInsumoRequestDTO {
    private Integer idInsumo;
    private Integer cantidadNecesaria;

    // Constructores
    public PlatilloInsumoRequestDTO() {}

    public PlatilloInsumoRequestDTO(Integer idInsumo, Integer cantidadNecesaria) {
        this.idInsumo = idInsumo;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    // Getters y Setters
    public Integer getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Integer idInsumo) { this.idInsumo = idInsumo; }

    public Integer getCantidadNecesaria() { return cantidadNecesaria; }
    public void setCantidadNecesaria(Integer cantidadNecesaria) { this.cantidadNecesaria = cantidadNecesaria; }
}
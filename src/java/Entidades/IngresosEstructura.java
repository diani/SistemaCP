/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;

/**
 *
 * @author diani
 */
public class IngresosEstructura implements Serializable{
    private PresentacionProducto preprod;
    private Float precio;
    private Integer cant;

    public IngresosEstructura(PresentacionProducto preprod, Float precio, Integer cant) {
        this.preprod = preprod;
        this.precio = precio;
        this.cant = cant;
    }

    public IngresosEstructura() {
    }

    public PresentacionProducto getPreprod() {
        return preprod;
    }

    public void setPreprod(PresentacionProducto preprod) {
        this.preprod = preprod;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }
    
    
}

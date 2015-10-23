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
    private Float cant;
    private Float costo;

    public IngresosEstructura(PresentacionProducto preprod, Float precio, Float cant) {
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

    public Float getCant() {
        return cant;
    }

    public void setCant(Float cant) {
        this.cant = cant;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }
    
    
}

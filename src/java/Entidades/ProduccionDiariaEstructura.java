/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author diani
 */
public class ProduccionDiariaEstructura implements Serializable{
 
    private Date prodDiaEstFecha;
    private String nombresCompletos;
    private Integer codigoUsuario;
    private List<ProduccionDiaria> lstProdDia;
    private String cantidadFruta;

    public ProduccionDiariaEstructura(Date prodDiaEstFecha, String nombresCompletos, Integer codigoUsuario) {
        this.prodDiaEstFecha = prodDiaEstFecha;
        this.nombresCompletos = nombresCompletos;
        this.codigoUsuario = codigoUsuario;
    }

    public Date getProdDiaEstFecha() {
        return prodDiaEstFecha;
    }

    public void setProdDiaEstFecha(Date prodDiaEstFecha) {
        this.prodDiaEstFecha = prodDiaEstFecha;
    }


    public List<ProduccionDiaria> getLstProdDia() {
        return lstProdDia;
    }

    public void setLstProdDia(List<ProduccionDiaria> lstProdDia) {
        this.lstProdDia = lstProdDia;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getCantidadFruta() {
        return cantidadFruta;
    }

    public void setCantidadFruta(String cantidadFruta) {
        this.cantidadFruta = cantidadFruta;
    }
    
}

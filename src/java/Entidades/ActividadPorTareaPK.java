/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author diani
 */
@Embeddable
public class ActividadPorTareaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROC_ACT_CODIGO")
    private int procActCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TAR_CODIGO")
    private int tarCodigo;

    public ActividadPorTareaPK() {
    }

    public ActividadPorTareaPK(int procActCodigo, int tarCodigo) {
        this.procActCodigo = procActCodigo;
        this.tarCodigo = tarCodigo;
    }

    public int getProcActCodigo() {
        return procActCodigo;
    }

    public void setProcActCodigo(int procActCodigo) {
        this.procActCodigo = procActCodigo;
    }

    public int getTarCodigo() {
        return tarCodigo;
    }

    public void setTarCodigo(int tarCodigo) {
        this.tarCodigo = tarCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) procActCodigo;
        hash += (int) tarCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadPorTareaPK)) {
            return false;
        }
        ActividadPorTareaPK other = (ActividadPorTareaPK) object;
        if (this.procActCodigo != other.procActCodigo) {
            return false;
        }
        if (this.tarCodigo != other.tarCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ActividadPorTareaPK[ procActCodigo=" + procActCodigo + ", tarCodigo=" + tarCodigo + " ]";
    }
    
}

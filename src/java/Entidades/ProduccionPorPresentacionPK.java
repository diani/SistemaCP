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
public class ProduccionPorPresentacionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROD_DIA_CODIGO")
    private int prodDiaCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRE_PROD_CODIGO")
    private int preProdCodigo;

    public ProduccionPorPresentacionPK() {
    }

    public ProduccionPorPresentacionPK(int prodDiaCodigo, int preProdCodigo) {
        this.prodDiaCodigo = prodDiaCodigo;
        this.preProdCodigo = preProdCodigo;
    }

    public int getProdDiaCodigo() {
        return prodDiaCodigo;
    }

    public void setProdDiaCodigo(int prodDiaCodigo) {
        this.prodDiaCodigo = prodDiaCodigo;
    }

    public int getPreProdCodigo() {
        return preProdCodigo;
    }

    public void setPreProdCodigo(int preProdCodigo) {
        this.preProdCodigo = preProdCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) prodDiaCodigo;
        hash += (int) preProdCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduccionPorPresentacionPK)) {
            return false;
        }
        ProduccionPorPresentacionPK other = (ProduccionPorPresentacionPK) object;
        if (this.prodDiaCodigo != other.prodDiaCodigo) {
            return false;
        }
        if (this.preProdCodigo != other.preProdCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ProduccionPorPresentacionPK[ prodDiaCodigo=" + prodDiaCodigo + ", preProdCodigo=" + preProdCodigo + " ]";
    }
    
}

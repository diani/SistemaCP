/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "produccion_por_presentacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduccionPorPresentacion.findAll", query = "SELECT p FROM ProduccionPorPresentacion p"),
    @NamedQuery(name = "ProduccionPorPresentacion.findByProdDiaCodigo", query = "SELECT p FROM ProduccionPorPresentacion p WHERE p.produccionPorPresentacionPK.prodDiaCodigo = :prodDiaCodigo"),
    @NamedQuery(name = "ProduccionPorPresentacion.findByPreProdCodigo", query = "SELECT p FROM ProduccionPorPresentacion p WHERE p.produccionPorPresentacionPK.preProdCodigo = :preProdCodigo"),
    @NamedQuery(name = "ProduccionPorPresentacion.findByProdPorPresCantPt", query = "SELECT p FROM ProduccionPorPresentacion p WHERE p.prodPorPresCantPt = :prodPorPresCantPt")})
public class ProduccionPorPresentacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProduccionPorPresentacionPK produccionPorPresentacionPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PROD_POR_PRES_CANT_PT")
    private Float prodPorPresCantPt;
    @JoinColumn(name = "PROD_DIA_CODIGO", referencedColumnName = "PROD_DIA_CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProduccionDiaria produccionDiaria;
    @JoinColumn(name = "PRE_PROD_CODIGO", referencedColumnName = "PRE_PROD_CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PresentacionProducto presentacionProducto;

    public ProduccionPorPresentacion() {
    }

    public ProduccionPorPresentacion(ProduccionPorPresentacionPK produccionPorPresentacionPK) {
        this.produccionPorPresentacionPK = produccionPorPresentacionPK;
    }

    public ProduccionPorPresentacion(int prodDiaCodigo, int preProdCodigo) {
        this.produccionPorPresentacionPK = new ProduccionPorPresentacionPK(prodDiaCodigo, preProdCodigo);
    }

    public ProduccionPorPresentacionPK getProduccionPorPresentacionPK() {
        return produccionPorPresentacionPK;
    }

    public void setProduccionPorPresentacionPK(ProduccionPorPresentacionPK produccionPorPresentacionPK) {
        this.produccionPorPresentacionPK = produccionPorPresentacionPK;
    }

    public Float getProdPorPresCantPt() {
        return prodPorPresCantPt;
    }

    public void setProdPorPresCantPt(Float prodPorPresCantPt) {
        this.prodPorPresCantPt = prodPorPresCantPt;
    }

    public ProduccionDiaria getProduccionDiaria() {
        return produccionDiaria;
    }

    public void setProduccionDiaria(ProduccionDiaria produccionDiaria) {
        this.produccionDiaria = produccionDiaria;
    }

    public PresentacionProducto getPresentacionProducto() {
        return presentacionProducto;
    }

    public void setPresentacionProducto(PresentacionProducto presentacionProducto) {
        this.presentacionProducto = presentacionProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produccionPorPresentacionPK != null ? produccionPorPresentacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduccionPorPresentacion)) {
            return false;
        }
        ProduccionPorPresentacion other = (ProduccionPorPresentacion) object;
        if ((this.produccionPorPresentacionPK == null && other.produccionPorPresentacionPK != null) || (this.produccionPorPresentacionPK != null && !this.produccionPorPresentacionPK.equals(other.produccionPorPresentacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ProduccionPorPresentacion[ produccionPorPresentacionPK=" + produccionPorPresentacionPK + " ]";
    }
    
}

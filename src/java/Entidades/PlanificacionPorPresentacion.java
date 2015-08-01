/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "planificacion_por_presentacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanificacionPorPresentacion.findAll", query = "SELECT p FROM PlanificacionPorPresentacion p"),
    @NamedQuery(name = "PlanificacionPorPresentacion.findByPlaPreCodigo", query = "SELECT p FROM PlanificacionPorPresentacion p WHERE p.plaPreCodigo = :plaPreCodigo"),
    @NamedQuery(name = "PlanificacionPorPresentacion.findByPlaPreCantidad", query = "SELECT p FROM PlanificacionPorPresentacion p WHERE p.plaPreCantidad = :plaPreCantidad")})
public class PlanificacionPorPresentacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PLA_PRE_CODIGO")
    private Integer plaPreCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PLA_PRE_CANTIDAD")
    private Float plaPreCantidad;
    @JoinColumn(name = "PRE_PROD_CODIGO", referencedColumnName = "PRE_PROD_CODIGO")
    @ManyToOne(optional = false)
    private PresentacionProducto preProdCodigo;
    @JoinColumn(name = "PLA_PROC_CODIGO", referencedColumnName = "PLA_PROC_CODIGO")
    @ManyToOne(optional = false)
    private PlanificacionProcesos plaProcCodigo;

    public PlanificacionPorPresentacion() {
    }

    public PlanificacionPorPresentacion(Integer plaPreCodigo) {
        this.plaPreCodigo = plaPreCodigo;
    }

    public Integer getPlaPreCodigo() {
        return plaPreCodigo;
    }

    public void setPlaPreCodigo(Integer plaPreCodigo) {
        this.plaPreCodigo = plaPreCodigo;
    }

    public Float getPlaPreCantidad() {
        return plaPreCantidad;
    }

    public void setPlaPreCantidad(Float plaPreCantidad) {
        this.plaPreCantidad = plaPreCantidad;
    }

    public PresentacionProducto getPreProdCodigo() {
        return preProdCodigo;
    }

    public void setPreProdCodigo(PresentacionProducto preProdCodigo) {
        this.preProdCodigo = preProdCodigo;
    }

    public PlanificacionProcesos getPlaProcCodigo() {
        return plaProcCodigo;
    }

    public void setPlaProcCodigo(PlanificacionProcesos plaProcCodigo) {
        this.plaProcCodigo = plaProcCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaPreCodigo != null ? plaPreCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanificacionPorPresentacion)) {
            return false;
        }
        PlanificacionPorPresentacion other = (PlanificacionPorPresentacion) object;
        if ((this.plaPreCodigo == null && other.plaPreCodigo != null) || (this.plaPreCodigo != null && !this.plaPreCodigo.equals(other.plaPreCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PlanificacionPorPresentacion[ plaPreCodigo=" + plaPreCodigo + " ]";
    }
    
}

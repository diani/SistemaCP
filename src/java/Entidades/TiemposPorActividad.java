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
@Table(name = "tiempos_por_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiemposPorActividad.findAll", query = "SELECT t FROM TiemposPorActividad t"),
    @NamedQuery(name = "TiemposPorActividad.findByTieActCodigo", query = "SELECT t FROM TiemposPorActividad t WHERE t.tieActCodigo = :tieActCodigo"),
    @NamedQuery(name = "TiemposPorActividad.findByTieActCantidad", query = "SELECT t FROM TiemposPorActividad t WHERE t.tieActCantidad = :tieActCantidad"),
    @NamedQuery(name = "TiemposPorActividad.findByTieActTiempo", query = "SELECT t FROM TiemposPorActividad t WHERE t.tieActTiempo = :tieActTiempo"),
    @NamedQuery(name = "TiemposPorActividad.findByTieActNumPersonas", query = "SELECT t FROM TiemposPorActividad t WHERE t.tieActNumPersonas = :tieActNumPersonas")})
public class TiemposPorActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TIE_ACT_CODIGO")
    private Integer tieActCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TIE_ACT_CANTIDAD")
    private Float tieActCantidad;
    @Column(name = "TIE_ACT_TIEMPO")
    private Float tieActTiempo;
    @Column(name = "TIE_ACT_NUM_PERSONAS")
    private Integer tieActNumPersonas;
    @JoinColumn(name = "PROC_ACT_CODIGO", referencedColumnName = "PROC_ACT_CODIGO")
    @ManyToOne(optional = false)
    private ProcesoPorActividad procActCodigo;
    @JoinColumn(name = "TIE_PROC_CODIGO", referencedColumnName = "TIE_PROC_CODIGO")
    @ManyToOne(optional = false)
    private TiemposProceso tieProcCodigo;

    public TiemposPorActividad() {
    }

    public TiemposPorActividad(Integer tieActCodigo) {
        this.tieActCodigo = tieActCodigo;
    }

    public Integer getTieActCodigo() {
        return tieActCodigo;
    }

    public void setTieActCodigo(Integer tieActCodigo) {
        this.tieActCodigo = tieActCodigo;
    }

    public Float getTieActCantidad() {
        return tieActCantidad;
    }

    public void setTieActCantidad(Float tieActCantidad) {
        this.tieActCantidad = tieActCantidad;
    }

    public Float getTieActTiempo() {
        return tieActTiempo;
    }

    public void setTieActTiempo(Float tieActTiempo) {
        this.tieActTiempo = tieActTiempo;
    }

    public Integer getTieActNumPersonas() {
        return tieActNumPersonas;
    }

    public void setTieActNumPersonas(Integer tieActNumPersonas) {
        this.tieActNumPersonas = tieActNumPersonas;
    }

    public ProcesoPorActividad getProcActCodigo() {
        return procActCodigo;
    }

    public void setProcActCodigo(ProcesoPorActividad procActCodigo) {
        this.procActCodigo = procActCodigo;
    }

    public TiemposProceso getTieProcCodigo() {
        return tieProcCodigo;
    }

    public void setTieProcCodigo(TiemposProceso tieProcCodigo) {
        this.tieProcCodigo = tieProcCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tieActCodigo != null ? tieActCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiemposPorActividad)) {
            return false;
        }
        TiemposPorActividad other = (TiemposPorActividad) object;
        if ((this.tieActCodigo == null && other.tieActCodigo != null) || (this.tieActCodigo != null && !this.tieActCodigo.equals(other.tieActCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiemposPorActividad[ tieActCodigo=" + tieActCodigo + " ]";
    }
    
}

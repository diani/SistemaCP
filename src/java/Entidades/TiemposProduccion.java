/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "tiempos_produccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiemposProduccion.findAll", query = "SELECT t FROM TiemposProduccion t"),
    @NamedQuery(name = "TiemposProduccion.findByTieProdCodigo", query = "SELECT t FROM TiemposProduccion t WHERE t.tieProdCodigo = :tieProdCodigo"),
    @NamedQuery(name = "TiemposProduccion.findByTieProdHoraIni", query = "SELECT t FROM TiemposProduccion t WHERE t.tieProdHoraIni = :tieProdHoraIni"),
    @NamedQuery(name = "TiemposProduccion.findByTieProdHoraFin", query = "SELECT t FROM TiemposProduccion t WHERE t.tieProdHoraFin = :tieProdHoraFin")})
public class TiemposProduccion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TIE_PROD_CODIGO")
    private Integer tieProdCodigo;
    @Column(name = "TIE_PROD_HORA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tieProdHoraIni;
    @Column(name = "TIE_PROD_HORA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tieProdHoraFin;
    @JoinColumn(name = "PLA_PROC_CODIGO", referencedColumnName = "PLA_PROC_CODIGO")
    @ManyToOne(optional = true)
    private PlanificacionProcesos plaProcCodigo;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;
    @Column(name = "TIE_PROD_FINAL")
    private Boolean tieProdFinal;
    

    public TiemposProduccion() {
    }

    public TiemposProduccion(Integer tieProdCodigo) {
        this.tieProdCodigo = tieProdCodigo;
    }

    public Integer getTieProdCodigo() {
        return tieProdCodigo;
    }

    public void setTieProdCodigo(Integer tieProdCodigo) {
        this.tieProdCodigo = tieProdCodigo;
    }

    public Date getTieProdHoraIni() {
        return tieProdHoraIni;
    }

    public void setTieProdHoraIni(Date tieProdHoraIni) {
        this.tieProdHoraIni = tieProdHoraIni;
    }

    public Date getTieProdHoraFin() {
        return tieProdHoraFin;
    }

    public void setTieProdHoraFin(Date tieProdHoraFin) {
        this.tieProdHoraFin = tieProdHoraFin;
    }

    public PlanificacionProcesos getPlaProcCodigo() {
        return plaProcCodigo;
    }

    public void setPlaProcCodigo(PlanificacionProcesos plaProcCodigo) {
        this.plaProcCodigo = plaProcCodigo;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tieProdCodigo != null ? tieProdCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiemposProduccion)) {
            return false;
        }
        TiemposProduccion other = (TiemposProduccion) object;
        if ((this.tieProdCodigo == null && other.tieProdCodigo != null) || (this.tieProdCodigo != null && !this.tieProdCodigo.equals(other.tieProdCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiemposProduccion[ tieProdCodigo=" + tieProdCodigo + " ]";
    }

    public Boolean getTieProdFinal() {
        return tieProdFinal;
    }

    public void setTieProdFinal(Boolean tieProdFinal) {
        this.tieProdFinal = tieProdFinal;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Controladores.util.JsfUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "tiempos_proceso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiemposProceso.findAll", query = "SELECT t FROM TiemposProceso t"),
    @NamedQuery(name = "TiemposProceso.findByTieProcCodigo", query = "SELECT t FROM TiemposProceso t WHERE t.tieProcCodigo = :tieProcCodigo"),
    @NamedQuery(name = "TiemposProceso.findByTieProcFecha", query = "SELECT t FROM TiemposProceso t WHERE t.tieProcFecha = :tieProcFecha")})
public class TiemposProceso implements Serializable {
    @Column(name = "TIE_PROC_FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date tieProcFechaFin;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TIE_PROC_CODIGO")
    private Integer tieProcCodigo;
    @Column(name = "TIE_PROC_FECHA")
    @Temporal(TemporalType.DATE)
    private Date tieProcFecha;
    @JoinColumn(name = "PROC_CODIGO", referencedColumnName = "PROC_CODIGO")
    @ManyToOne(optional = false)
    private Proceso procCodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tieProcCodigo")
    private List<TiemposPorActividad> tiemposPorActividadList;
    
    private transient int numeroSemana;
    
    public TiemposProceso() {
    }

    public TiemposProceso(Integer tieProcCodigo) {
        this.tieProcCodigo = tieProcCodigo;
    }

    public Integer getTieProcCodigo() {
        return tieProcCodigo;
    }

    public void setTieProcCodigo(Integer tieProcCodigo) {
        this.tieProcCodigo = tieProcCodigo;
    }

    public Date getTieProcFecha() {
        return tieProcFecha;
    }

    public void setTieProcFecha(Date tieProcFecha) {
        this.tieProcFecha = tieProcFecha;
    }

    public Proceso getProcCodigo() {
        return procCodigo;
    }

    public void setProcCodigo(Proceso procCodigo) {
        this.procCodigo = procCodigo;
    }

    @Transient
    public int getNumeroSemana() {
        if(tieProcFecha != null){
            Calendar cal = JsfUtil.DateToCalendar(tieProcFecha);
            numeroSemana = cal.get(Calendar.WEEK_OF_YEAR);
        }
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    
    
    @XmlTransient
    public List<TiemposPorActividad> getTiemposPorActividadList() {
        return tiemposPorActividadList;
    }

    public void setTiemposPorActividadList(List<TiemposPorActividad> tiemposPorActividadList) {
        this.tiemposPorActividadList = tiemposPorActividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tieProcCodigo != null ? tieProcCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiemposProceso)) {
            return false;
        }
        TiemposProceso other = (TiemposProceso) object;
        if ((this.tieProcCodigo == null && other.tieProcCodigo != null) || (this.tieProcCodigo != null && !this.tieProcCodigo.equals(other.tieProcCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiemposProceso[ tieProcCodigo=" + tieProcCodigo + " ]";
    }

    public Date getTieProcFechaFin() {
        return tieProcFechaFin;
    }

    public void setTieProcFechaFin(Date tieProcFechaFin) {
        this.tieProcFechaFin = tieProcFechaFin;
    }
    
}

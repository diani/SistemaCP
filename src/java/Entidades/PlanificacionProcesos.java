/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "planificacion_procesos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanificacionProcesos.findAll", query = "SELECT p FROM PlanificacionProcesos p"),
    @NamedQuery(name = "PlanificacionProcesos.findByPlaProcCodigo", query = "SELECT p FROM PlanificacionProcesos p WHERE p.plaProcCodigo = :plaProcCodigo"),
    @NamedQuery(name = "PlanificacionProcesos.findByPlaProcFechaIni", query = "SELECT p FROM PlanificacionProcesos p WHERE p.plaProcFechaIni = :plaProcFechaIni"),
    @NamedQuery(name = "PlanificacionProcesos.findByPlaProcFechaFin", query = "SELECT p FROM PlanificacionProcesos p WHERE p.plaProcFechaFin = :plaProcFechaFin")})
public class PlanificacionProcesos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PLA_PROC_CODIGO")
    private Integer plaProcCodigo;
    @Column(name = "PLA_PROC_FECHA_INI")
    @Temporal(TemporalType.DATE)
    private Date plaProcFechaIni;
    @Column(name = "PLA_PROC_FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date plaProcFechaFin;
    @Column(name = "PLA_PROC_HABILITADO")
    private Boolean plaProcHabilitado;
    @Column(name = "PLA_PROC_PLAY")
    private Boolean plaProcPlay;
    @JoinColumn(name = "PROC_CODIGO", referencedColumnName = "PROC_CODIGO")
    @ManyToOne(optional = false)
    private Proceso procCodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plaProcCodigo")
    private List<TiemposProduccion> tiemposProduccionList;

    public PlanificacionProcesos() {
    }

    public PlanificacionProcesos(Integer plaProcCodigo) {
        this.plaProcCodigo = plaProcCodigo;
    }

    public Integer getPlaProcCodigo() {
        return plaProcCodigo;
    }

    public void setPlaProcCodigo(Integer plaProcCodigo) {
        this.plaProcCodigo = plaProcCodigo;
    }

    public Date getPlaProcFechaIni() {
        return plaProcFechaIni;
    }

    public void setPlaProcFechaIni(Date plaProcFechaIni) {
        this.plaProcFechaIni = plaProcFechaIni;
    }

    public Date getPlaProcFechaFin() {
        return plaProcFechaFin;
    }

    public void setPlaProcFechaFin(Date plaProcFechaFin) {
        this.plaProcFechaFin = plaProcFechaFin;
    }

    public Proceso getProcCodigo() {
        return procCodigo;
    }

    public void setProcCodigo(Proceso procCodigo) {
        this.procCodigo = procCodigo;
    }
    public Boolean getPlaProcHabilitado() {
        return plaProcHabilitado;
    }

    public void setPlaProcHabilitado(Boolean plaProcHabilitado) {
        this.plaProcHabilitado = plaProcHabilitado;
    }

    @XmlTransient
    public List<TiemposProduccion> getTiemposProduccionList() {
        return tiemposProduccionList;
    }

    public void setTiemposProduccionList(List<TiemposProduccion> tiemposProduccionList) {
        this.tiemposProduccionList = tiemposProduccionList;
    }

    public Boolean getPlaProcPlay() {
        return plaProcPlay;
    }

    public void setPlaProcPlay(Boolean plaProcPlay) {
        this.plaProcPlay = plaProcPlay;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaProcCodigo != null ? plaProcCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanificacionProcesos)) {
            return false;
        }
        PlanificacionProcesos other = (PlanificacionProcesos) object;
        if ((this.plaProcCodigo == null && other.plaProcCodigo != null) || (this.plaProcCodigo != null && !this.plaProcCodigo.equals(other.plaProcCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PlanificacionProcesos[ plaProcCodigo=" + plaProcCodigo + " ]";
    }
    
}

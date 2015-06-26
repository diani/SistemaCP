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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "proceso_por_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesoPorActividad.findAll", query = "SELECT p FROM ProcesoPorActividad p"),
    @NamedQuery(name = "ProcesoPorActividad.findByProcActCodigo", query = "SELECT p FROM ProcesoPorActividad p WHERE p.procActCodigo = :procActCodigo"),
    @NamedQuery(name = "ProcesoPorActividad.findByProcActTiempo", query = "SELECT p FROM ProcesoPorActividad p WHERE p.procActTiempo = :procActTiempo"),
    @NamedQuery(name = "ProcesoPorActividad.findByProcActNumPersonas", query = "SELECT p FROM ProcesoPorActividad p WHERE p.procActNumPersonas = :procActNumPersonas")})
public class ProcesoPorActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROC_ACT_CODIGO")
    private Integer procActCodigo;
    @Column(name = "PROC_ACT_TIEMPO")
    private Float procActTiempo;
    @Column(name = "PROC_ACT_NUM_PERSONAS")
    private Integer procActNumPersonas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoPorActividad")
    private List<ActividadPorTarea> actividadPorTareaList;
    @JoinColumn(name = "ACT_CODIGO", referencedColumnName = "ACT_CODIGO")
    @ManyToOne(optional = false)
    private Actividad actCodigo;
    @JoinColumn(name = "PROC_CODIGO", referencedColumnName = "PROC_CODIGO")
    @ManyToOne(optional = false)
    private Proceso procCodigo;
    @Column(name = "PROC_ACT_ORDEN")
    private Integer procActOrden;
    private transient List<ActividadPorTarea> actividadPorTareaListTransient;

    @Id
    public Float getProcActTiempo() {
        return procActTiempo;
    }

    public void setProcActTiempo(Float procActTiempo) {
        this.procActTiempo = procActTiempo;
    }

    public Integer getProcActOrden() {
        return procActOrden;
    }

    public void setProcActOrden(Integer procActOrden) {
        this.procActOrden = procActOrden;
    }

    public ProcesoPorActividad() {
    }

    public ProcesoPorActividad(Integer procActCodigo) {
        this.procActCodigo = procActCodigo;
    }

    public Integer getProcActCodigo() {
        return procActCodigo;
    }

    public void setProcActCodigo(Integer procActCodigo) {
        this.procActCodigo = procActCodigo;
    }

    public Integer getProcActNumPersonas() {
        return procActNumPersonas;
    }

    public void setProcActNumPersonas(Integer procActNumPersonas) {
        this.procActNumPersonas = procActNumPersonas;
    }

    @XmlTransient
    public List<ActividadPorTarea> getActividadPorTareaList() {
        return actividadPorTareaList;
    }

    public void setActividadPorTareaList(List<ActividadPorTarea> actividadPorTareaList) {
        this.actividadPorTareaList = actividadPorTareaList;
    }

    public Actividad getActCodigo() {
        return actCodigo;
    }

    public void setActCodigo(Actividad actCodigo) {
        this.actCodigo = actCodigo;
    }

    public Proceso getProcCodigo() {
        return procCodigo;
    }

    public void setProcCodigo(Proceso procCodigo) {
        this.procCodigo = procCodigo;
    }

    @Transient
    public List<ActividadPorTarea> getActividadPorTareaListTransient() {
        if(actividadPorTareaList != null && !actividadPorTareaList.isEmpty())
        {
            actividadPorTareaListTransient = actividadPorTareaList;
        }
        return actividadPorTareaListTransient;
    }

    public void setActividadPorTareaListTransient(List<ActividadPorTarea> actividadPorTareaListTransient) {
        this.actividadPorTareaListTransient = actividadPorTareaListTransient;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procActCodigo != null ? procActCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesoPorActividad)) {
            return false;
        }
        ProcesoPorActividad other = (ProcesoPorActividad) object;
        if ((this.procActCodigo == null && other.procActCodigo != null) || (this.procActCodigo != null && !this.procActCodigo.equals(other.procActCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ProcesoPorActividad[ procActCodigo=" + procActCodigo + " ]";
    }
    
}

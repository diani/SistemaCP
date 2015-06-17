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
@Table(name = "actividad_por_tarea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadPorTarea.findAll", query = "SELECT a FROM ActividadPorTarea a"),
    @NamedQuery(name = "ActividadPorTarea.findByProcActCodigo", query = "SELECT a FROM ActividadPorTarea a WHERE a.actividadPorTareaPK.procActCodigo = :procActCodigo"),
    @NamedQuery(name = "ActividadPorTarea.findByTarCodigo", query = "SELECT a FROM ActividadPorTarea a WHERE a.actividadPorTareaPK.tarCodigo = :tarCodigo"),
    @NamedQuery(name = "ActividadPorTarea.findByActTarHabilitado", query = "SELECT a FROM ActividadPorTarea a WHERE a.actTarHabilitado = :actTarHabilitado")})
public class ActividadPorTarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActividadPorTareaPK actividadPorTareaPK;
    @Column(name = "ACT_TAR_HABILITADO")
    private Boolean actTarHabilitado;
    @JoinColumn(name = "PROC_ACT_CODIGO", referencedColumnName = "PROC_ACT_CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProcesoPorActividad procesoPorActividad;
    @JoinColumn(name = "TAR_CODIGO", referencedColumnName = "TAR_CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tarea tarea;

    public ActividadPorTarea() {
    }

    public ActividadPorTarea(ActividadPorTareaPK actividadPorTareaPK) {
        this.actividadPorTareaPK = actividadPorTareaPK;
    }

    public ActividadPorTarea(int procActCodigo, int tarCodigo) {
        this.actividadPorTareaPK = new ActividadPorTareaPK(procActCodigo, tarCodigo);
    }

    public ActividadPorTareaPK getActividadPorTareaPK() {
        return actividadPorTareaPK;
    }

    public void setActividadPorTareaPK(ActividadPorTareaPK actividadPorTareaPK) {
        this.actividadPorTareaPK = actividadPorTareaPK;
    }

    public Boolean getActTarHabilitado() {
        return actTarHabilitado;
    }

    public void setActTarHabilitado(Boolean actTarHabilitado) {
        this.actTarHabilitado = actTarHabilitado;
    }

    public ProcesoPorActividad getProcesoPorActividad() {
        return procesoPorActividad;
    }

    public void setProcesoPorActividad(ProcesoPorActividad procesoPorActividad) {
        this.procesoPorActividad = procesoPorActividad;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actividadPorTareaPK != null ? actividadPorTareaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadPorTarea)) {
            return false;
        }
        ActividadPorTarea other = (ActividadPorTarea) object;
        if ((this.actividadPorTareaPK == null && other.actividadPorTareaPK != null) || (this.actividadPorTareaPK != null && !this.actividadPorTareaPK.equals(other.actividadPorTareaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ActividadPorTarea[ actividadPorTareaPK=" + actividadPorTareaPK + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "tarea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarea.findAll", query = "SELECT t FROM Tarea t"),
    @NamedQuery(name = "Tarea.findByTarCodigo", query = "SELECT t FROM Tarea t WHERE t.tarCodigo = :tarCodigo"),
    @NamedQuery(name = "Tarea.findByTarDescripcion", query = "SELECT t FROM Tarea t WHERE t.tarDescripcion = :tarDescripcion"),
    @NamedQuery(name = "Tarea.findByTarHabilitado", query = "SELECT t FROM Tarea t WHERE t.tarHabilitado = :tarHabilitado")})
public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TAR_CODIGO")
    private Integer tarCodigo;
    @Size(max = 255)
    @Column(name = "TAR_DESCRIPCION")
    private String tarDescripcion;
    @Column(name = "TAR_HABILITADO")
    private Boolean tarHabilitado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarea")
    private List<ActividadPorTarea> actividadPorTareaList;

    public Tarea() {
    }

    public Tarea(Integer tarCodigo) {
        this.tarCodigo = tarCodigo;
    }

    public Integer getTarCodigo() {
        return tarCodigo;
    }

    public void setTarCodigo(Integer tarCodigo) {
        this.tarCodigo = tarCodigo;
    }

    public String getTarDescripcion() {
        return tarDescripcion;
    }

    public void setTarDescripcion(String tarDescripcion) {
        this.tarDescripcion = tarDescripcion;
    }

    public Boolean getTarHabilitado() {
        return tarHabilitado;
    }

    public void setTarHabilitado(Boolean tarHabilitado) {
        this.tarHabilitado = tarHabilitado;
    }

    @XmlTransient
    public List<ActividadPorTarea> getActividadPorTareaList() {
        return actividadPorTareaList;
    }

    public void setActividadPorTareaList(List<ActividadPorTarea> actividadPorTareaList) {
        this.actividadPorTareaList = actividadPorTareaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarCodigo != null ? tarCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarea)) {
            return false;
        }
        Tarea other = (Tarea) object;
        if ((this.tarCodigo == null && other.tarCodigo != null) || (this.tarCodigo != null && !this.tarCodigo.equals(other.tarCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tarea[ tarCodigo=" + tarCodigo + " ]";
    }
    
}

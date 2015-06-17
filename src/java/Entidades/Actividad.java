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
@Table(name = "actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findByActCodigo", query = "SELECT a FROM Actividad a WHERE a.actCodigo = :actCodigo"),
    @NamedQuery(name = "Actividad.findByActDescripcion", query = "SELECT a FROM Actividad a WHERE a.actDescripcion = :actDescripcion"),
    @NamedQuery(name = "Actividad.findByActHabilitado", query = "SELECT a FROM Actividad a WHERE a.actHabilitado = :actHabilitado")})
public class Actividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACT_CODIGO")
    private Integer actCodigo;
    @Size(max = 255)
    @Column(name = "ACT_DESCRIPCION")
    private String actDescripcion;
    @Column(name = "ACT_HABILITADO")
    private Boolean actHabilitado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actCodigo")
    private List<ProcesoPorActividad> procesoPorActividadList;

    public Actividad() {
    }

    public Actividad(Integer actCodigo) {
        this.actCodigo = actCodigo;
    }

    public Integer getActCodigo() {
        return actCodigo;
    }

    public void setActCodigo(Integer actCodigo) {
        this.actCodigo = actCodigo;
    }

    public String getActDescripcion() {
        return actDescripcion;
    }

    public void setActDescripcion(String actDescripcion) {
        this.actDescripcion = actDescripcion;
    }

    public Boolean getActHabilitado() {
        return actHabilitado;
    }

    public void setActHabilitado(Boolean actHabilitado) {
        this.actHabilitado = actHabilitado;
    }

    @XmlTransient
    public List<ProcesoPorActividad> getProcesoPorActividadList() {
        return procesoPorActividadList;
    }

    public void setProcesoPorActividadList(List<ProcesoPorActividad> procesoPorActividadList) {
        this.procesoPorActividadList = procesoPorActividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actCodigo != null ? actCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.actCodigo == null && other.actCodigo != null) || (this.actCodigo != null && !this.actCodigo.equals(other.actCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Actividad[ actCodigo=" + actCodigo + " ]";
    }
    
}

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "proceso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proceso.findAll", query = "SELECT p FROM Proceso p"),
    @NamedQuery(name = "Proceso.findByProcCodigo", query = "SELECT p FROM Proceso p WHERE p.procCodigo = :procCodigo"),
    @NamedQuery(name = "Proceso.findByProcMateriaPrima", query = "SELECT p FROM Proceso p WHERE p.procMateriaPrima = :procMateriaPrima"),
    @NamedQuery(name = "Proceso.findByProcProductoTerminado", query = "SELECT p FROM Proceso p WHERE p.procProductoTerminado = :procProductoTerminado"),
    @NamedQuery(name = "Proceso.findByProcHabilitadoInterno", query = "SELECT p FROM Proceso p WHERE p.procHabilitadoInterno = :procHabilitadoInterno")})
public class Proceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROC_CODIGO")
    private Integer procCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PROC_MATERIA_PRIMA")
    private Float procMateriaPrima;
    @Column(name = "PROC_PRODUCTO_TERMINADO")
    private Float procProductoTerminado;
    @Column(name = "PROC_HABILITADO_INTERNO")
    private Boolean procHabilitadoInterno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procCodigo")
    private List<PlanificacionProcesos> planificacionProcesosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procCodigo")
    private List<ProcesoPorActividad> procesoPorActividadList;
    @JoinColumn(name = "PROD_CODIGO", referencedColumnName = "PROD_CODIGO")
    @ManyToOne(optional = false)
    private Producto prodCodigo;

    public Proceso() {
    }

    public Proceso(Integer procCodigo) {
        this.procCodigo = procCodigo;
    }

    public Integer getProcCodigo() {
        return procCodigo;
    }

    public void setProcCodigo(Integer procCodigo) {
        this.procCodigo = procCodigo;
    }

    public Float getProcMateriaPrima() {
        return procMateriaPrima;
    }

    public void setProcMateriaPrima(Float procMateriaPrima) {
        this.procMateriaPrima = procMateriaPrima;
    }

    public Float getProcProductoTerminado() {
        return procProductoTerminado;
    }

    public void setProcProductoTerminado(Float procProductoTerminado) {
        this.procProductoTerminado = procProductoTerminado;
    }

    public Boolean getProcHabilitadoInterno() {
        return procHabilitadoInterno;
    }

    public void setProcHabilitadoInterno(Boolean procHabilitadoInterno) {
        this.procHabilitadoInterno = procHabilitadoInterno;
    }

    @XmlTransient
    public List<PlanificacionProcesos> getPlanificacionProcesosList() {
        return planificacionProcesosList;
    }

    public void setPlanificacionProcesosList(List<PlanificacionProcesos> planificacionProcesosList) {
        this.planificacionProcesosList = planificacionProcesosList;
    }

    @XmlTransient
    public List<ProcesoPorActividad> getProcesoPorActividadList() {
        return procesoPorActividadList;
    }

    public void setProcesoPorActividadList(List<ProcesoPorActividad> procesoPorActividadList) {
        this.procesoPorActividadList = procesoPorActividadList;
    }

    public Producto getProdCodigo() {
        return prodCodigo;
    }

    public void setProdCodigo(Producto prodCodigo) {
        this.prodCodigo = prodCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procCodigo != null ? procCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proceso)) {
            return false;
        }
        Proceso other = (Proceso) object;
        if ((this.procCodigo == null && other.procCodigo != null) || (this.procCodigo != null && !this.procCodigo.equals(other.procCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Proceso[ procCodigo=" + procCodigo + " ]";
    }
    
}

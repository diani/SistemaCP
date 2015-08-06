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
@Table(name = "cif")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cif.findAll", query = "SELECT c FROM Cif c"),
    @NamedQuery(name = "Cif.findByCifCodigo", query = "SELECT c FROM Cif c WHERE c.cifCodigo = :cifCodigo"),
    @NamedQuery(name = "Cif.findByCifDescripcion", query = "SELECT c FROM Cif c WHERE c.cifDescripcion = :cifDescripcion")})
public class Cif implements Serializable {
    @Column(name = "CIF_HABILITADO")
    private Boolean cifHabilitado;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CIF_CODIGO")
    private Integer cifCodigo;
    @Size(max = 255)
    @Column(name = "CIF_DESCRIPCION")
    private String cifDescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cifCodigo")
    private List<UsuarioPorCif> usuarioPorCifList;

    public Cif() {
    }

    public Cif(Integer cifCodigo) {
        this.cifCodigo = cifCodigo;
    }

    public Integer getCifCodigo() {
        return cifCodigo;
    }

    public void setCifCodigo(Integer cifCodigo) {
        this.cifCodigo = cifCodigo;
    }

    public String getCifDescripcion() {
        return cifDescripcion;
    }

    public void setCifDescripcion(String cifDescripcion) {
        this.cifDescripcion = cifDescripcion;
    }

    @XmlTransient
    public List<UsuarioPorCif> getUsuarioPorCifList() {
        return usuarioPorCifList;
    }

    public void setUsuarioPorCifList(List<UsuarioPorCif> usuarioPorCifList) {
        this.usuarioPorCifList = usuarioPorCifList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cifCodigo != null ? cifCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cif)) {
            return false;
        }
        Cif other = (Cif) object;
        if ((this.cifCodigo == null && other.cifCodigo != null) || (this.cifCodigo != null && !this.cifCodigo.equals(other.cifCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cif[ cifCodigo=" + cifCodigo + " ]";
    }

    public Boolean getCifHabilitado() {
        return cifHabilitado;
    }

    public void setCifHabilitado(Boolean cifHabilitado) {
        this.cifHabilitado = cifHabilitado;
    }
    
}

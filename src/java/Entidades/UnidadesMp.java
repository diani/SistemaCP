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
@Table(name = "unidades_mp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadesMp.findAll", query = "SELECT u FROM UnidadesMp u"),
    @NamedQuery(name = "UnidadesMp.findByUniMpCodigo", query = "SELECT u FROM UnidadesMp u WHERE u.uniMpCodigo = :uniMpCodigo"),
    @NamedQuery(name = "UnidadesMp.findByUniMpDescripcion", query = "SELECT u FROM UnidadesMp u WHERE u.uniMpDescripcion = :uniMpDescripcion"),
    @NamedQuery(name = "UnidadesMp.findByUniMpHabilitado", query = "SELECT u FROM UnidadesMp u WHERE u.uniMpHabilitado = :uniMpHabilitado"),
    @NamedQuery(name = "UnidadesMp.findByUniMpEquivalenciaMp", query = "SELECT u FROM UnidadesMp u WHERE u.uniMpEquivalenciaMp = :uniMpEquivalenciaMp")})
public class UnidadesMp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UNI_MP_CODIGO")
    private Integer uniMpCodigo;
    @Size(max = 255)
    @Column(name = "UNI_MP_DESCRIPCION")
    private String uniMpDescripcion;
    @Column(name = "UNI_MP_HABILITADO")
    private Boolean uniMpHabilitado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNI_MP_EQUIVALENCIA_MP")
    private Float uniMpEquivalenciaMp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniMpCodigo")
    private List<ProduccionDiaria> produccionDiariaList;

    public UnidadesMp() {
    }

    public UnidadesMp(Integer uniMpCodigo) {
        this.uniMpCodigo = uniMpCodigo;
    }

    public Integer getUniMpCodigo() {
        return uniMpCodigo;
    }

    public void setUniMpCodigo(Integer uniMpCodigo) {
        this.uniMpCodigo = uniMpCodigo;
    }

    public String getUniMpDescripcion() {
        return uniMpDescripcion;
    }

    public void setUniMpDescripcion(String uniMpDescripcion) {
        this.uniMpDescripcion = uniMpDescripcion;
    }

    public Boolean getUniMpHabilitado() {
        return uniMpHabilitado;
    }

    public void setUniMpHabilitado(Boolean uniMpHabilitado) {
        this.uniMpHabilitado = uniMpHabilitado;
    }

    public Float getUniMpEquivalenciaMp() {
        return uniMpEquivalenciaMp;
    }

    public void setUniMpEquivalenciaMp(Float uniMpEquivalenciaMp) {
        this.uniMpEquivalenciaMp = uniMpEquivalenciaMp;
    }

    @XmlTransient
    public List<ProduccionDiaria> getProduccionDiariaList() {
        return produccionDiariaList;
    }

    public void setProduccionDiariaList(List<ProduccionDiaria> produccionDiariaList) {
        this.produccionDiariaList = produccionDiariaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uniMpCodigo != null ? uniMpCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadesMp)) {
            return false;
        }
        UnidadesMp other = (UnidadesMp) object;
        if ((this.uniMpCodigo == null && other.uniMpCodigo != null) || (this.uniMpCodigo != null && !this.uniMpCodigo.equals(other.uniMpCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UnidadesMp[ uniMpCodigo=" + uniMpCodigo + " ]";
    }
    
}

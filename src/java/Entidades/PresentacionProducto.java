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
@Table(name = "presentacion_producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PresentacionProducto.findAll", query = "SELECT p FROM PresentacionProducto p"),
    @NamedQuery(name = "PresentacionProducto.findByPreProdCodigo", query = "SELECT p FROM PresentacionProducto p WHERE p.preProdCodigo = :preProdCodigo"),
    @NamedQuery(name = "PresentacionProducto.findByPreProdDescripcion", query = "SELECT p FROM PresentacionProducto p WHERE p.preProdDescripcion = :preProdDescripcion"),
    @NamedQuery(name = "PresentacionProducto.findByPreProdHabilitado", query = "SELECT p FROM PresentacionProducto p WHERE p.preProdHabilitado = :preProdHabilitado"),
    @NamedQuery(name = "PresentacionProducto.findByPreProdEquivalenciaPt", query = "SELECT p FROM PresentacionProducto p WHERE p.preProdEquivalenciaPt = :preProdEquivalenciaPt")})
public class PresentacionProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRE_PROD_CODIGO")
    private Integer preProdCodigo;
    @Size(max = 255)
    @Column(name = "PRE_PROD_DESCRIPCION")
    private String preProdDescripcion;
    @Column(name = "PRE_PROD_HABILITADO")
    private Boolean preProdHabilitado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRE_PROD_EQUIVALENCIA_PT")
    private Float preProdEquivalenciaPt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentacionProducto")
    private List<ProduccionPorPresentacion> produccionPorPresentacionList;

    public PresentacionProducto() {
    }

    public PresentacionProducto(Integer preProdCodigo) {
        this.preProdCodigo = preProdCodigo;
    }

    public Integer getPreProdCodigo() {
        return preProdCodigo;
    }

    public void setPreProdCodigo(Integer preProdCodigo) {
        this.preProdCodigo = preProdCodigo;
    }

    public String getPreProdDescripcion() {
        return preProdDescripcion;
    }

    public void setPreProdDescripcion(String preProdDescripcion) {
        this.preProdDescripcion = preProdDescripcion;
    }

    public Boolean getPreProdHabilitado() {
        return preProdHabilitado;
    }

    public void setPreProdHabilitado(Boolean preProdHabilitado) {
        this.preProdHabilitado = preProdHabilitado;
    }

    public Float getPreProdEquivalenciaPt() {
        return preProdEquivalenciaPt;
    }

    public void setPreProdEquivalenciaPt(Float preProdEquivalenciaPt) {
        this.preProdEquivalenciaPt = preProdEquivalenciaPt;
    }

    @XmlTransient
    public List<ProduccionPorPresentacion> getProduccionPorPresentacionList() {
        return produccionPorPresentacionList;
    }

    public void setProduccionPorPresentacionList(List<ProduccionPorPresentacion> produccionPorPresentacionList) {
        this.produccionPorPresentacionList = produccionPorPresentacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (preProdCodigo != null ? preProdCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PresentacionProducto)) {
            return false;
        }
        PresentacionProducto other = (PresentacionProducto) object;
        if ((this.preProdCodigo == null && other.preProdCodigo != null) || (this.preProdCodigo != null && !this.preProdCodigo.equals(other.preProdCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PresentacionProducto[ preProdCodigo=" + preProdCodigo + " ]";
    }
    
}

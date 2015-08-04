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
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByProdCodigo", query = "SELECT p FROM Producto p WHERE p.prodCodigo = :prodCodigo"),
    @NamedQuery(name = "Producto.findByProdCodigoAux", query = "SELECT p FROM Producto p WHERE p.prodCodigoAux = :prodCodigoAux"),
    @NamedQuery(name = "Producto.findByProdDescripcion", query = "SELECT p FROM Producto p WHERE p.prodDescripcion = :prodDescripcion"),
    @NamedQuery(name = "Producto.findByProdHabilitado", query = "SELECT p FROM Producto p WHERE p.prodHabilitado = :prodHabilitado")})
public class Producto implements Serializable {
    @Column(name = "PROD_ACCION")
    private Boolean prodAccion;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROD_CODIGO")
    private Integer prodCodigo;
    @Size(max = 255)
    @Column(name = "PROD_CODIGO_AUX")
    private String prodCodigoAux;
    @Size(max = 255)
    @Column(name = "PROD_DESCRIPCION")
    private String prodDescripcion;
    @Size(max = 255)
    @Column(name = "PROD_IMAGEN")
    private String prodImagen;
    @Column(name = "PROD_HABILITADO")
    private Boolean prodHabilitado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prodCodigo")
    private List<Proceso> procesoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prodCodigo")
    private List<ProduccionDiaria> produccionDiariaList;

    public Producto() {
    }

    public Producto(Integer prodCodigo) {
        this.prodCodigo = prodCodigo;
    }

    public Integer getProdCodigo() {
        return prodCodigo;
    }

    public void setProdCodigo(Integer prodCodigo) {
        this.prodCodigo = prodCodigo;
    }

    public String getProdCodigoAux() {
        return prodCodigoAux;
    }

    public void setProdCodigoAux(String prodCodigoAux) {
        this.prodCodigoAux = prodCodigoAux;
    }

    public String getProdDescripcion() {
        return prodDescripcion;
    }

    public void setProdDescripcion(String prodDescripcion) {
        this.prodDescripcion = prodDescripcion;
    }

    public String getProdImagen() {
        return prodImagen;
    }

    public void setProdImagen(String prodImagen) {
        this.prodImagen = prodImagen;
    }
    
    public Boolean getProdHabilitado() {
        return prodHabilitado;
    }

    public void setProdHabilitado(Boolean prodHabilitado) {
        this.prodHabilitado = prodHabilitado;
    }

    @XmlTransient
    public List<Proceso> getProcesoList() {
        return procesoList;
    }

    public void setProcesoList(List<Proceso> procesoList) {
        this.procesoList = procesoList;
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
        hash += (prodCodigo != null ? prodCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.prodCodigo == null && other.prodCodigo != null) || (this.prodCodigo != null && !this.prodCodigo.equals(other.prodCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Producto[ prodCodigo=" + prodCodigo + " ]";
    }

    public Boolean getProdAccion() {
        return prodAccion;
    }

    public void setProdAccion(Boolean prodAccion) {
        this.prodAccion = prodAccion;
    }
    
}

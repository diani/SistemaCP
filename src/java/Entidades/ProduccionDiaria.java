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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "produccion_diaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduccionDiaria.findAll", query = "SELECT p FROM ProduccionDiaria p"),
    @NamedQuery(name = "ProduccionDiaria.findByProdDiaCodigo", query = "SELECT p FROM ProduccionDiaria p WHERE p.prodDiaCodigo = :prodDiaCodigo"),
    @NamedQuery(name = "ProduccionDiaria.findByProdDiaFecha", query = "SELECT p FROM ProduccionDiaria p WHERE p.prodDiaFecha = :prodDiaFecha"),
    @NamedQuery(name = "ProduccionDiaria.findByProdDiaLote", query = "SELECT p FROM ProduccionDiaria p WHERE p.prodDiaLote = :prodDiaLote"),
    @NamedQuery(name = "ProduccionDiaria.findByProdDiaCantMp", query = "SELECT p FROM ProduccionDiaria p WHERE p.prodDiaCantMp = :prodDiaCantMp")})
public class ProduccionDiaria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROD_DIA_CODIGO")
    private Integer prodDiaCodigo;
    @Column(name = "PROD_DIA_FECHA")
    @Temporal(TemporalType.DATE)
    private Date prodDiaFecha;
    @Size(max = 255)
    @Column(name = "PROD_DIA_LOTE")
    private String prodDiaLote;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PROD_DIA_CANT_MP")
    private Float prodDiaCantMp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produccionDiaria")
    private List<ProduccionPorPresentacion> produccionPorPresentacionList;
    @JoinColumn(name = "PROD_CODIGO", referencedColumnName = "PROD_CODIGO")
    @ManyToOne(optional = false)
    private Producto prodCodigo;
    @JoinColumn(name = "UNI_MP_CODIGO", referencedColumnName = "UNI_MP_CODIGO")
    @ManyToOne(optional = false)
    private UnidadesMp uniMpCodigo;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;
    private transient Float totalProdT;
    
    public ProduccionDiaria() {
    }

    public ProduccionDiaria(Integer prodDiaCodigo) {
        this.prodDiaCodigo = prodDiaCodigo;
    }

    public Integer getProdDiaCodigo() {
        return prodDiaCodigo;
    }

    public void setProdDiaCodigo(Integer prodDiaCodigo) {
        this.prodDiaCodigo = prodDiaCodigo;
    }

    public Date getProdDiaFecha() {
        return prodDiaFecha;
    }

    public void setProdDiaFecha(Date prodDiaFecha) {
        this.prodDiaFecha = prodDiaFecha;
    }

    public String getProdDiaLote() {
        return prodDiaLote;
    }

    public void setProdDiaLote(String prodDiaLote) {
        this.prodDiaLote = prodDiaLote;
    }

    public Float getProdDiaCantMp() {
        return prodDiaCantMp;
    }

    public void setProdDiaCantMp(Float prodDiaCantMp) {
        this.prodDiaCantMp = prodDiaCantMp;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Transient
    public Float getTotalProdT() {
        return totalProdT;
    }

    public void setTotalProdT(Float totalProdT) {
        this.totalProdT = totalProdT;
    }

    
    @XmlTransient
    public List<ProduccionPorPresentacion> getProduccionPorPresentacionList() {
        return produccionPorPresentacionList;
    }

    public void setProduccionPorPresentacionList(List<ProduccionPorPresentacion> produccionPorPresentacionList) {
        this.produccionPorPresentacionList = produccionPorPresentacionList;
    }

    public Producto getProdCodigo() {
        return prodCodigo;
    }

    public void setProdCodigo(Producto prodCodigo) {
        this.prodCodigo = prodCodigo;
    }

    public UnidadesMp getUniMpCodigo() {
        return uniMpCodigo;
    }

    public void setUniMpCodigo(UnidadesMp uniMpCodigo) {
        this.uniMpCodigo = uniMpCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prodDiaCodigo != null ? prodDiaCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduccionDiaria)) {
            return false;
        }
        ProduccionDiaria other = (ProduccionDiaria) object;
        if ((this.prodDiaCodigo == null && other.prodDiaCodigo != null) || (this.prodDiaCodigo != null && !this.prodDiaCodigo.equals(other.prodDiaCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ProduccionDiaria[ prodDiaCodigo=" + prodDiaCodigo + " ]";
    }
    
}

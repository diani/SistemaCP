/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "usuario_por_cif")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioPorCif.findAll", query = "SELECT u FROM UsuarioPorCif u"),
    @NamedQuery(name = "UsuarioPorCif.findByUsuCifCodigo", query = "SELECT u FROM UsuarioPorCif u WHERE u.usuCifCodigo = :usuCifCodigo"),
    @NamedQuery(name = "UsuarioPorCif.findByUsuCifCantidad", query = "SELECT u FROM UsuarioPorCif u WHERE u.usuCifCantidad = :usuCifCantidad"),
    @NamedQuery(name = "UsuarioPorCif.findByUsuCifCosto", query = "SELECT u FROM UsuarioPorCif u WHERE u.usuCifCosto = :usuCifCosto")})
public class UsuarioPorCif implements Serializable {
    @Column(name = "USU_CIF_FECHA")
    @Temporal(TemporalType.DATE)
    private Date usuCifFecha;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USU_CIF_CODIGO")
    private Integer usuCifCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "USU_CIF_CANTIDAD")
    private Float usuCifCantidad;
    @Column(name = "USU_CIF_COSTO")
    private Float usuCifCosto;
    @JoinColumn(name = "CIF_CODIGO", referencedColumnName = "CIF_CODIGO")
    @ManyToOne(optional = false)
    private Cif cifCodigo;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public UsuarioPorCif() {
    }

    public UsuarioPorCif(Integer usuCifCodigo) {
        this.usuCifCodigo = usuCifCodigo;
    }

    public Integer getUsuCifCodigo() {
        return usuCifCodigo;
    }

    public void setUsuCifCodigo(Integer usuCifCodigo) {
        this.usuCifCodigo = usuCifCodigo;
    }

    public Float getUsuCifCantidad() {
        return usuCifCantidad;
    }

    public void setUsuCifCantidad(Float usuCifCantidad) {
        this.usuCifCantidad = usuCifCantidad;
    }

    public Float getUsuCifCosto() {
        return usuCifCosto;
    }

    public void setUsuCifCosto(Float usuCifCosto) {
        this.usuCifCosto = usuCifCosto;
    }

    public Cif getCifCodigo() {
        return cifCodigo;
    }

    public void setCifCodigo(Cif cifCodigo) {
        this.cifCodigo = cifCodigo;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuCifCodigo != null ? usuCifCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPorCif)) {
            return false;
        }
        UsuarioPorCif other = (UsuarioPorCif) object;
        if ((this.usuCifCodigo == null && other.usuCifCodigo != null) || (this.usuCifCodigo != null && !this.usuCifCodigo.equals(other.usuCifCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UsuarioPorCif[ usuCifCodigo=" + usuCifCodigo + " ]";
    }

    public Date getUsuCifFecha() {
        return usuCifFecha;
    }

    public void setUsuCifFecha(Date usuCifFecha) {
        this.usuCifFecha = usuCifFecha;
    }
    
}

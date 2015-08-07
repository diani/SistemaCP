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
@Table(name = "usuario_por_material_embalaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioPorMaterialEmbalaje.findAll", query = "SELECT u FROM UsuarioPorMaterialEmbalaje u"),
    @NamedQuery(name = "UsuarioPorMaterialEmbalaje.findByUsuMatEmbCodigo", query = "SELECT u FROM UsuarioPorMaterialEmbalaje u WHERE u.usuMatEmbCodigo = :usuMatEmbCodigo"),
    @NamedQuery(name = "UsuarioPorMaterialEmbalaje.findByUsuMatEmbCosto", query = "SELECT u FROM UsuarioPorMaterialEmbalaje u WHERE u.usuMatEmbCosto = :usuMatEmbCosto"),
    @NamedQuery(name = "UsuarioPorMaterialEmbalaje.findByUsuMatEmbCostoUni", query = "SELECT u FROM UsuarioPorMaterialEmbalaje u WHERE u.usuMatEmbCostoUni = :usuMatEmbCostoUni")})
public class UsuarioPorMaterialEmbalaje implements Serializable {
    @Column(name = "USU_MAT_EMB_FECHA")
    @Temporal(TemporalType.DATE)
    private Date usuMatEmbFecha;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USU_MAT_EMB_CODIGO")
    private Integer usuMatEmbCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "USU_MAT_EMB_COSTO")
    private Float usuMatEmbCosto;
    @Column(name = "USU_MAT_EMB_COSTO_UNI")
    private Float usuMatEmbCostoUni;
    @JoinColumn(name = "MAT_EMB_CODIGO", referencedColumnName = "MAT_EMB_CODIGO")
    @ManyToOne(optional = false)
    private MaterialEmbalaje matEmbCodigo;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
    @ManyToOne(optional = false)
    private Usuario usuId;

    public UsuarioPorMaterialEmbalaje() {
    }

    public UsuarioPorMaterialEmbalaje(Integer usuMatEmbCodigo) {
        this.usuMatEmbCodigo = usuMatEmbCodigo;
    }

    public Integer getUsuMatEmbCodigo() {
        return usuMatEmbCodigo;
    }

    public void setUsuMatEmbCodigo(Integer usuMatEmbCodigo) {
        this.usuMatEmbCodigo = usuMatEmbCodigo;
    }

    public Float getUsuMatEmbCosto() {
        return usuMatEmbCosto;
    }

    public void setUsuMatEmbCosto(Float usuMatEmbCosto) {
        this.usuMatEmbCosto = usuMatEmbCosto;
    }

    public Float getUsuMatEmbCostoUni() {
        return usuMatEmbCostoUni;
    }

    public void setUsuMatEmbCostoUni(Float usuMatEmbCostoUni) {
        this.usuMatEmbCostoUni = usuMatEmbCostoUni;
    }

    public MaterialEmbalaje getMatEmbCodigo() {
        return matEmbCodigo;
    }

    public void setMatEmbCodigo(MaterialEmbalaje matEmbCodigo) {
        this.matEmbCodigo = matEmbCodigo;
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
        hash += (usuMatEmbCodigo != null ? usuMatEmbCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPorMaterialEmbalaje)) {
            return false;
        }
        UsuarioPorMaterialEmbalaje other = (UsuarioPorMaterialEmbalaje) object;
        if ((this.usuMatEmbCodigo == null && other.usuMatEmbCodigo != null) || (this.usuMatEmbCodigo != null && !this.usuMatEmbCodigo.equals(other.usuMatEmbCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.UsuarioPorMaterialEmbalaje[ usuMatEmbCodigo=" + usuMatEmbCodigo + " ]";
    }

    public Date getUsuMatEmbFecha() {
        return usuMatEmbFecha;
    }

    public void setUsuMatEmbFecha(Date usuMatEmbFecha) {
        this.usuMatEmbFecha = usuMatEmbFecha;
    }
    
}

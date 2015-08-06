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
@Table(name = "material_embalaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaterialEmbalaje.findAll", query = "SELECT m FROM MaterialEmbalaje m"),
    @NamedQuery(name = "MaterialEmbalaje.findByMatEmbCodigo", query = "SELECT m FROM MaterialEmbalaje m WHERE m.matEmbCodigo = :matEmbCodigo"),
    @NamedQuery(name = "MaterialEmbalaje.findByMatEmbDescripcion", query = "SELECT m FROM MaterialEmbalaje m WHERE m.matEmbDescripcion = :matEmbDescripcion")})
public class MaterialEmbalaje implements Serializable {
    @Column(name = "MAT_EMB_HABILITADO")
    private Boolean matEmbHabilitado;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MAT_EMB_CODIGO")
    private Integer matEmbCodigo;
    @Size(max = 255)
    @Column(name = "MAT_EMB_DESCRIPCION")
    private String matEmbDescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matEmbCodigo")
    private List<UsuarioPorMaterialEmbalaje> usuarioPorMaterialEmbalajeList;

    public MaterialEmbalaje() {
    }

    public MaterialEmbalaje(Integer matEmbCodigo) {
        this.matEmbCodigo = matEmbCodigo;
    }

    public Integer getMatEmbCodigo() {
        return matEmbCodigo;
    }

    public void setMatEmbCodigo(Integer matEmbCodigo) {
        this.matEmbCodigo = matEmbCodigo;
    }

    public String getMatEmbDescripcion() {
        return matEmbDescripcion;
    }

    public void setMatEmbDescripcion(String matEmbDescripcion) {
        this.matEmbDescripcion = matEmbDescripcion;
    }

    @XmlTransient
    public List<UsuarioPorMaterialEmbalaje> getUsuarioPorMaterialEmbalajeList() {
        return usuarioPorMaterialEmbalajeList;
    }

    public void setUsuarioPorMaterialEmbalajeList(List<UsuarioPorMaterialEmbalaje> usuarioPorMaterialEmbalajeList) {
        this.usuarioPorMaterialEmbalajeList = usuarioPorMaterialEmbalajeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matEmbCodigo != null ? matEmbCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaterialEmbalaje)) {
            return false;
        }
        MaterialEmbalaje other = (MaterialEmbalaje) object;
        if ((this.matEmbCodigo == null && other.matEmbCodigo != null) || (this.matEmbCodigo != null && !this.matEmbCodigo.equals(other.matEmbCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.MaterialEmbalaje[ matEmbCodigo=" + matEmbCodigo + " ]";
    }

    public Boolean getMatEmbHabilitado() {
        return matEmbHabilitado;
    }

    public void setMatEmbHabilitado(Boolean matEmbHabilitado) {
        this.matEmbHabilitado = matEmbHabilitado;
    }
    
}

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diani
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsuId", query = "SELECT u FROM Usuario u WHERE u.usuId = :usuId"),
    @NamedQuery(name = "Usuario.findByUsuNombre", query = "SELECT u FROM Usuario u WHERE u.usuNombre = :usuNombre"),
    @NamedQuery(name = "Usuario.findByUsuApellido", query = "SELECT u FROM Usuario u WHERE u.usuApellido = :usuApellido"),
    @NamedQuery(name = "Usuario.findByUsuUsuario", query = "SELECT u FROM Usuario u WHERE u.usuUsuario = :usuUsuario"),
    @NamedQuery(name = "Usuario.findByUsuClave", query = "SELECT u FROM Usuario u WHERE u.usuClave = :usuClave"),
    @NamedQuery(name = "Usuario.findByUsuHabilitado", query = "SELECT u FROM Usuario u WHERE u.usuHabilitado = :usuHabilitado")})
public class Usuario implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuId")
    private List<UsuarioPorMaterialEmbalaje> usuarioPorMaterialEmbalajeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuId")
    private List<UsuarioPorCif> usuarioPorCifList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USU_ID")
    private Integer usuId;
    @Size(max = 255)
    @Column(name = "USU_NOMBRE")
    private String usuNombre;
    @Size(max = 255)
    @Column(name = "USU_APELLIDO")
    private String usuApellido;
    @Size(max = 255)
    @Column(name = "USU_USUARIO")
    private String usuUsuario;
    @Size(max = 255)
    @Column(name = "USU_CLAVE")
    private String usuClave;
    @Column(name = "USU_HABILITADO")
    private Boolean usuHabilitado;
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ROL_ID")
    @ManyToOne(optional = false)
    private Rol rolId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuId")
    private List<TiemposProduccion> tiemposProduccionList;

    public Usuario() {
    }

    public Usuario(Integer usuId) {
        this.usuId = usuId;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuApellido() {
        return usuApellido;
    }

    public void setUsuApellido(String usuApellido) {
        this.usuApellido = usuApellido;
    }

    public String getUsuUsuario() {
        return usuUsuario;
    }

    public void setUsuUsuario(String usuUsuario) {
        this.usuUsuario = usuUsuario;
    }

    public String getUsuClave() {
        return usuClave;
    }

    public void setUsuClave(String usuClave) {
        this.usuClave = usuClave;
    }

    public Boolean getUsuHabilitado() {
        return usuHabilitado;
    }

    public void setUsuHabilitado(Boolean usuHabilitado) {
        this.usuHabilitado = usuHabilitado;
    }

    public Rol getRolId() {
        return rolId;
    }

    public void setRolId(Rol rolId) {
        this.rolId = rolId;
    }

    @XmlTransient
    public List<TiemposProduccion> getTiemposProduccionList() {
        return tiemposProduccionList;
    }

    public void setTiemposProduccionList(List<TiemposProduccion> tiemposProduccionList) {
        this.tiemposProduccionList = tiemposProduccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuId != null ? usuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuId == null && other.usuId != null) || (this.usuId != null && !this.usuId.equals(other.usuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Usuario[ usuId=" + usuId + " ]";
    }

    @XmlTransient
    public List<UsuarioPorMaterialEmbalaje> getUsuarioPorMaterialEmbalajeList() {
        return usuarioPorMaterialEmbalajeList;
    }

    public void setUsuarioPorMaterialEmbalajeList(List<UsuarioPorMaterialEmbalaje> usuarioPorMaterialEmbalajeList) {
        this.usuarioPorMaterialEmbalajeList = usuarioPorMaterialEmbalajeList;
    }

    @XmlTransient
    public List<UsuarioPorCif> getUsuarioPorCifList() {
        return usuarioPorCifList;
    }

    public void setUsuarioPorCifList(List<UsuarioPorCif> usuarioPorCifList) {
        this.usuarioPorCifList = usuarioPorCifList;
    }
    
}

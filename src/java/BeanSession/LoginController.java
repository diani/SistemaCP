/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanSession;

import Entidades.Usuario;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.jms.Session;
import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;


/**
 *
 * @author diani
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable{

    /**
     * Creates a new instance of LoginController
     */
    
    private String usuario;
    private String contrasenia;
    private Session session;
    private Transaction transaccion;
    private Usuario Usuactivo;
    @EJB
    private Controladores.UsuarioFacade ejbUsuFacade;
    
    
    public LoginController() {
        HttpSession miSession=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(5000);
    }
    public String login()
    {
        this.session=null;
        this.transaccion=null;
        
        try{
            Usuactivo=new Usuario();
            if(usuario != null && !usuario.isEmpty() && contrasenia!=null && !contrasenia.isEmpty()){
                Usuactivo=ejbUsuFacade.UsuClave(usuario, contrasenia, true);
            }
            
            if(Usuactivo!=null)
            {
                HttpSession httpSession=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                httpSession.setAttribute("usuario", this.Usuactivo);
                
                if(Usuactivo.getRolId().getRolDescripcion().equals("ADMINISTRADOR")){
                    return "/admin/crud/home";
                }else{
                    return "/tiemposProduccion/tiempos";
                }
            }
           
            this.usuario=null;
            this.contrasenia=null;
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "Usuario o contraseña incorrecto"));
            return "/index";            
        }
        catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador "+ex.getMessage()));
            return null;
        }
    }
    
    public String cerrarSesion(){
        this.usuario=null;
        this.contrasenia=null;
        this.Usuactivo=null;
        HttpSession httpSession=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.invalidate();
        return "/index";
    }
 
    public String getContrasenia(){
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Transaction getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaction transaccion) {
        this.transaccion = transaccion;
    }

    public Usuario getUsuactivo() {
        return Usuactivo;
    }

    public void setUsuactivo(Usuario Usuactivo) {
        this.Usuactivo = Usuactivo;
    }
     public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

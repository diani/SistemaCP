/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import BeanSession.LoginController;
import Controladores.util.JsfUtil;
import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author diani
 */

@ManagedBean(name = "tiemposController")
@SessionScoped
public class TiemposController implements Serializable {

    @EJB
    private Controladores.PlanificacionFacade ejbPlaniProcFacade;
    @EJB
    private Controladores.TiemposFacade ejbTiemposFacade;
    @EJB
    private Controladores.PlanificacionPorPresentacionFacade ejbPlaniPresFacade;
    private List<PlanificacionProcesos> planiproc = null;
    
    private PlanificacionProcesos selectedplaproc;
    private TiemposProduccion selectedtiempos = new TiemposProduccion();
    private Boolean playBandera;
    private Boolean almuerzo;

    public void init(Usuario user) {
         reproductor(user);
    }
    
    public int usuariosTrabajando(PlanificacionProcesos plpr){
        List<TiemposProduccion> tipro = ejbTiemposFacade.tiempoPorPlanificacion(plpr);
        List<Usuario> us=new ArrayList<Usuario>();
        if(tipro!=null && !tipro.isEmpty()){
            for(TiemposProduccion tp : tipro){
                if(!us.contains(tp.getUsuId())){
                    us.add(tp.getUsuId());
                }
            }
        }
        plpr.setPersonasTrabajando(us.size());
        return plpr.getPersonasTrabajando();
    }
    
    public void reproductor(Usuario usuarioActivo){
        selectedtiempos=new TiemposProduccion();
//        selectedtiempos.setPlaProcCodigo(selectedplaproc);
//        selectedplaproc.setPersonasTrabajando(this.usuariosTrabajando(selectedplaproc));
        selectedtiempos.setUsuId(usuarioActivo);
        TiemposProduccion tiempos = ejbTiemposFacade.ultimoTiempoPorUsuarioYdia(usuarioActivo);
        if(tiempos == null){
            playBandera = true;
        }else{
            if(tiempos.getTieProdHoraIni() != null && !tiempos.getTieProdFinal()){
                playBandera = false;
                almuerzo = true;
            }else if(tiempos.getTieProdHoraFin()!= null && !tiempos.getTieProdFinal()){
                playBandera = false;
                almuerzo = false;
            }else{
                playBandera = true;
            }
        }
    }
    
    public void play(){
        try{
            selectedtiempos.setTieProdHoraIni(new Date());
            selectedtiempos.setTieProdFinal(false);
            ejbTiemposFacade.persist(selectedtiempos);
            reproductor(selectedtiempos.getUsuId());
            JsfUtil.addSuccessMessage("Se registro la Hora de ingreso Satisfcatoriamente");
        }catch(Exception e){
            JsfUtil.addErrorMessage("No se pudo registra La Hora de ingreso correctamente");
        }
        
        
    }
    
    public void pause(){
        try{
            selectedtiempos.setTieProdHoraFin(new Date());
            selectedtiempos.setTieProdFinal(false);
            ejbTiemposFacade.persist(selectedtiempos);
            reproductor(selectedtiempos.getUsuId());
            JsfUtil.addSuccessMessage("Se registro la Hora de Almuerzo Satisfcatoriamente");
        }catch(Exception e){
            JsfUtil.addErrorMessage("No se pudo registra La Hora de Almuerzo correctamente");
        }
        
    }
    
    public void stop(){
        try{
            selectedtiempos.setTieProdHoraFin(new Date());
            selectedtiempos.setTieProdFinal(true);
            ejbTiemposFacade.persist(selectedtiempos);
            JsfUtil.addSuccessMessage("Se registro la Hora de Salida Satisfcatoriamente");
        }catch(Exception e){
            JsfUtil.addErrorMessage("No se pudo registra La Hora de Salida correctamente");
        }
        
    }
    
    public PlanificacionProcesos getSelectedplaproc() {
        return selectedplaproc;
    }

    public void setSelectedplaproc(PlanificacionProcesos selectedplaproc) {
        this.selectedplaproc = selectedplaproc;
    }
    
    public List<PlanificacionProcesos> getPlaniproc() {
        planiproc = ejbPlaniProcFacade.buscarFechaIni();
        for(PlanificacionProcesos plp: planiproc){
            plp.setPlanificacionPorPresentacionList(ejbPlaniPresFacade.buscarPlaPorPrePorParamPlani(plp));
        }
        return planiproc;
    }

    public void setPlaniproc(List<PlanificacionProcesos> planiproc) {
        this.planiproc = planiproc;
    }
    
     public TiemposProduccion getSelectedtiempos() {
        return selectedtiempos;
    }

    public void setSelectedtiempos(TiemposProduccion selectedtiempos) {
        this.selectedtiempos = selectedtiempos;
    }

    public Boolean getPlayBandera() {
        return playBandera;
    }

    public void setPlayBandera(Boolean playBandera) {
        this.playBandera = playBandera;
    }

    public Boolean getAlmuerzo() {
        return almuerzo;
    }

    public void setAlmuerzo(Boolean almuerzo) {
        this.almuerzo = almuerzo;
    }

    
    
    
}

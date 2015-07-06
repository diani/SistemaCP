/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import BeanSession.LoginController;
import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private List<PlanificacionProcesos> planiproc = null;
    
    private PlanificacionProcesos selectedplaproc;
    private TiemposProduccion selectedtiempos = new TiemposProduccion();

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
        return us.size();
    }
    
    public void reproductor(Usuario usuarioActivo){
        selectedtiempos=new TiemposProduccion();
        selectedtiempos.setPlaProcCodigo(selectedplaproc);
        selectedtiempos.setUsuId(usuarioActivo);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ReproductorDialog').show();");
    }
    
    public void play(){
        selectedtiempos.setTieProdHoraIni(new Date());
        ejbTiemposFacade.persist(selectedtiempos);
        selectedplaproc.setPlaProcPlay(true);
        ejbPlaniProcFacade.merge(selectedplaproc);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ReproductorDialog').hide();");
    }
    public void pause(){
        selectedtiempos.setTieProdHoraFin(new Date());
        ejbTiemposFacade.persist(selectedtiempos);
        selectedplaproc.setPlaProcPlay(false);
        ejbPlaniProcFacade.merge(selectedplaproc);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ReproductorDialog').hide();");
    }
    
    public void stop(){
        selectedtiempos.setTieProdHoraFin(new Date());
        ejbTiemposFacade.persist(selectedtiempos);
        selectedplaproc.setPlaProcHabilitado(false);
        selectedplaproc.setPlaProcPlay(false);
        ejbPlaniProcFacade.merge(selectedplaproc);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ReproductorDialog').hide();");
    }
    
    public PlanificacionProcesos getSelectedplaproc() {
        return selectedplaproc;
    }

    public void setSelectedplaproc(PlanificacionProcesos selectedplaproc) {
        this.selectedplaproc = selectedplaproc;
    }
    
    public List<PlanificacionProcesos> getPlaniproc() {
        planiproc = ejbPlaniProcFacade.buscarFechaIni();
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
}
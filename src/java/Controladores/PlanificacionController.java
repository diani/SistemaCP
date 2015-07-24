/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.PlanificacionProcesos;
import Entidades.Proceso;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import static org.jboss.weld.logging.BeanLogger.LOG;
import org.primefaces.context.RequestContext;

/**
 *
 * @author diani
 */
@ManagedBean(name = "planificacionController")
@SessionScoped
public class PlanificacionController implements Serializable {
    
    @EJB
    private Controladores.PlanificacionFacade ejbPlaniFacade;
    @EJB
    private Controladores.ProcesoFacade ejbProcFacade;
    private List<PlanificacionProcesos> lstPlaniProc = null;
    private List<Proceso> lstProc = null;
    private PlanificacionProcesos planiProcSeleccionado;

    public void guardarPlanificacion(){
        if(planiProcSeleccionado.getProcCodigo().getProcCodigo() !=null && planiProcSeleccionado.getPlaProcFechaFin() != null && planiProcSeleccionado.getPlaProcFechaIni() != null){    
            try{
                if(planiProcSeleccionado.getPlaProcCodigo() == null)
                {
                    ejbPlaniFacade.persist(planiProcSeleccionado);
                }else
                    ejbPlaniFacade.merge(planiProcSeleccionado);
                lstPlaniProc.add(planiProcSeleccionado);
                JsfUtil.addSuccessMessage("Guardado Correctamente");
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('PlaniDialog').hide();");
            }catch(Exception ex){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }else{
            JsfUtil.addErrorMessage("Los datos marcados con * son requeridos");
        }
            
    }
    
    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("RolDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            planiProcSeleccionado = null; // Remove selection
            lstPlaniProc = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (planiProcSeleccionado != null) {
            try {
                planiProcSeleccionado.setPlaProcHabilitado(new Boolean(false));
                ejbPlaniFacade.edit(planiProcSeleccionado);
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public void crearPlanificacion(){
        planiProcSeleccionado = new PlanificacionProcesos();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('PlaniDialog').show();");        
    }
    
    public boolean filterByDate(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        if (value == null) {
            return false;
        }

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date filterDate = (Date) value;
        Date dateFrom;
        Date dateTo;
        try {
            String fromPart = filterText.substring(0, filterText.indexOf("-"));
            String toPart = filterText.substring(filterText.indexOf("-") + 1);
            dateFrom = fromPart.isEmpty() ? null : df.parse(fromPart);
            dateTo = toPart.isEmpty() ? null : df.parse(toPart);
            dateFrom=sumarRestarDiasFecha(dateFrom, -1);
            dateTo=sumarRestarDiasFecha(dateTo, 1);
        } catch (ParseException pe) {
            return false;
        }

        return (dateFrom == null || filterDate.after(dateFrom)) && (dateTo == null || filterDate.before(dateTo));
    }
    
     public Date sumarRestarDiasFecha(Date fecha, int dias){
 
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(fecha); // Configuramos la fecha que se recibe
      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
 
      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos 
    }
    
    public List<PlanificacionProcesos> getLstPlaniProc() {
        lstPlaniProc = ejbPlaniFacade.buscarTodosOrdenadosFecha();
        return lstPlaniProc;
    }

    public void setLstPlaniProc(List<PlanificacionProcesos> lstPlaniProc) {
        this.lstPlaniProc = lstPlaniProc;
    }

    public List<Proceso> getLstProc() {
        lstProc = ejbProcFacade.buscarTodosPorHabiltiado(true);
        return lstProc;
    }

    public void setLstProc(List<Proceso> lstProc) {
        this.lstProc = lstProc;
    }
     public PlanificacionProcesos getPlaniProcSeleccionado() {
        return planiProcSeleccionado;
    }

    public void setPlaniProcSeleccionado(PlanificacionProcesos planiProcSeleccionado) {
        this.planiProcSeleccionado = planiProcSeleccionado;
    }

}

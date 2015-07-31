/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Proceso;
import Controladores.util.JsfUtil;
import Controladores.util.JsfUtil.PersistAction;
import Entidades.Actividad;
import Entidades.ActividadPorTarea;
import Entidades.ActividadPorTareaPK;
import Entidades.ProcesoPorActividad;
import Entidades.Producto;
import Entidades.Tarea;
import Entidades.TiemposPorActividad;
import Entidades.TiemposProceso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "tiemposProcesoController")
@SessionScoped
public class TiemposProcesoController implements Serializable {

    @EJB
    private Controladores.ProcesoFacade ejbFacade;
    @EJB
    private Controladores.TiemposPorActividadFacade ejbTieActFacade;
    @EJB
    private Controladores.TiemposProcesoFacade ejbTieProcFacade;
    @EJB
    private Controladores.ActividadFacade ejbActFacade;
    @EJB
    private Controladores.ProcesoPorActividadFacade ejbProcActFacade;
    @EJB
    private Controladores.ProductoFacade ejbProdFacade;
    @EJB
    private Controladores.TareaFacade ejbTarFacade;
    @EJB
    private Controladores.ActividadPorTareaFacade ejbActTarFacade;
    
    private List<TiemposProceso> items = null;
    private TiemposProceso selected; //proceso seleccionado
    private Boolean crear;
    private ProcesoPorActividad selectedPA; // proceso por atividad seleccionado
    private ActividadPorTarea selectedAT; // actividad por tarea seleccionado
    private List<Actividad> actizq = new ArrayList<Actividad>();
    private List<Actividad> actder = new ArrayList<Actividad>();
    private DualListModel<Actividad> actividades = new DualListModel<Actividad>(actizq,actder);
    private List<Proceso> lstproceso = null;
    private List<Tarea> tarizq = new ArrayList<Tarea>();
    private List<Tarea> tarder = new ArrayList<Tarea>();
    private DualListModel<Tarea> tareas = new DualListModel<Tarea>(tarizq,tarder);
    private TreeNode arbolProceso;
    
    public TiemposProcesoController() {
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
    
    public TiemposProceso getSelected() {
        return selected;
    }

    public void setSelected(TiemposProceso selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProcesoFacade getFacade() {
        return ejbFacade;
    }
  
    public String editarTiempoProceso(){
        crear = false;
        lstproceso = ejbFacade.buscarTodosPorHabiltiado(true);
        selected.setTiemposPorActividadList(ejbTieActFacade.TiemposPorActividadPorTiempoProcesos(selected));
        return "/tiemposProceso/tiemposProceso.xhtml";
    }
    
    public String atras(){
        return "/tiemposProceso/principalTiemposProceso.xhtml";
    }
    
    public void cargarActividades(){
        selected.setTiemposPorActividadList(new ArrayList<TiemposPorActividad>());
        List<ProcesoPorActividad> lstprocAct = ejbProcActFacade.buscarListaDeProcesosActivdadPorProceso(selected.getProcCodigo());
        if(lstprocAct != null && !lstprocAct.isEmpty()){
            for(ProcesoPorActividad pract:lstprocAct){
                TiemposPorActividad tiact = new TiemposPorActividad();
                tiact.setProcActCodigo(pract);
                selected.getTiemposPorActividadList().add(tiact);
            }
        }
    }
    
    public String crearTiempoProceso(){
        selected =  new TiemposProceso();
        crear = true;
        lstproceso = ejbFacade.buscarTodosPorHabiltiado(true);
        return "/tiemposProceso/tiemposProceso.xhtml";
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProcesoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        selected = new TiemposProceso();
     
        return "/tiemposProceso/principalTiemposProceso.xhtml";
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProcesoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProcesoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda Cambiada", "Antigua: " + (oldValue != null ? oldValue : "-") + ", Nueva:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<TiemposProceso> getItems() {
        items =  ejbTieProcFacade.findAll();
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    List<TiemposPorActividad> lstTiemposActividad = selected.getTiemposPorActividadList();
                    selected.setTiemposPorActividadList(null);
                    //guardo mi proceso sin listas
                    if (selected.getTieProcCodigo() == null) {
                        selected = ejbTieProcFacade.persist(selected); //cuando no existe en la base (crear)
                    }else{
                        selected = ejbTieProcFacade.merge(selected); // cuando ya existe en la base (editar)
                    }
                    if(lstTiemposActividad != null && !lstTiemposActividad.isEmpty()){
                        for (TiemposPorActividad lstTiemposActividad1 : lstTiemposActividad) {
                            if(lstTiemposActividad1.getTieActCodigo() == null){
                                lstTiemposActividad1.setTieProcCodigo(selected);
                                ejbTieActFacade.persist(lstTiemposActividad1);
                            }else{
                                lstTiemposActividad1.setTieProcCodigo(selected);
                                ejbTieActFacade.merge(lstTiemposActividad1);
                            }
                        }
                    }
                    
                } else {
                    
                    ejbTieProcFacade.edit(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public List<Proceso> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Proceso> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public Boolean getCrear() {
        return crear;
    }

    public void setCrear(Boolean crear) {
        this.crear = crear;
    }

    public ProcesoPorActividad getSelectedPA() {
        return selectedPA;
    }

    public void setSelectedPA(ProcesoPorActividad selectedPA) {
        this.selectedPA = selectedPA;
    }
 
    public DualListModel<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(DualListModel<Actividad> actividades) {
        this.actividades = actividades;
    }

    public List<Actividad> getActizq() {
        return actizq;
    }

    public void setActizq(List<Actividad> actizq) {
        this.actizq = actizq;
    }

    public List<Actividad> getActder() {
        return actder;
    }

    public void setActder(List<Actividad> actder) {
        this.actder = actder;
    }
    
   
    
    public List<Tarea> getTarizq() {
        return tarizq;
    }

    public void setTarizq(List<Tarea> tarizq) {
        this.tarizq = tarizq;
    }

    public List<Tarea> getTarder() {
        return tarder;
    }

    public void setTarder(List<Tarea> tarder) {
        this.tarder = tarder;
    }

    public DualListModel<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(DualListModel<Tarea> tareas) {
        this.tareas = tareas;
    }

    public ActividadPorTarea getSelectedAT() {
        return selectedAT;
    }

    public void setSelectedAT(ActividadPorTarea selectedAT) {
        this.selectedAT = selectedAT;
    }

    public TreeNode getArbolProceso() {
        return arbolProceso;
    }

    public void setArbolProceso(TreeNode arbolProceso) {
        this.arbolProceso = arbolProceso;
    }

   
    public List<Proceso> getLstproceso() {
        return lstproceso;
    }

    public void setLstproceso(List<Proceso> lstproceso) {
        this.lstproceso = lstproceso;
    }
    
    
}

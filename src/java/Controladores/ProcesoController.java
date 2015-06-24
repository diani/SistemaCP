package Controladores;

import Entidades.Proceso;
import Controladores.util.JsfUtil;
import Controladores.util.JsfUtil.PersistAction;
import Entidades.Actividad;
import Entidades.ProcesoPorActividad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.primefaces.model.DualListModel;

@ManagedBean(name = "procesoController")
@SessionScoped
public class ProcesoController implements Serializable {

    @EJB
    private Controladores.ProcesoFacade ejbFacade;
    @EJB
    private Controladores.ActividadFacade ejbActFacade;
    private List<Proceso> items = null;
    private Proceso selected;
    private Boolean crear;
    private ProcesoPorActividad selectedPA;
    private List<Actividad> actizq = new ArrayList<Actividad>();
    private List<Actividad> actder = new ArrayList<Actividad>();
    private DualListModel<Actividad> actividades = new DualListModel<Actividad>(actizq,actder);
    
    
    public ProcesoController() {
    }

    public Proceso getSelected() {
        return selected;
    }

    public void setSelected(Proceso selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProcesoFacade getFacade() {
        return ejbFacade;
    }

    public Proceso prepareCreate() {
        selected = new Proceso();
        initializeEmbeddableKey();
        return selected;
    }
    
    public String editarProceso(){
        crear = false;
        return "/admin/crud/proceso/CrearProceso.xhtml";
    }
    
    public String crearProceso(){
        selected =  new Proceso();
        selected.setProcesoPorActividadList(new ArrayList<ProcesoPorActividad>());
        crear = true;
        return "/admin/crud/proceso/CrearProceso.xhtml";
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProcesoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
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

    public List<Proceso> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    selected.setProcHabilitadoInterno(new Boolean(false));
                    getFacade().edit(selected);
                }
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
    
    public void abrirActividades(){
        actizq = ejbActFacade.actividadeshabilitadas(true);
        actder = new ArrayList<Actividad>();
        for(ProcesoPorActividad var: selected.getProcesoPorActividadList()){
            actder.add(var.getActCodigo());
        }
        for(Actividad aux: actder){
            if(actizq.contains(aux)){
                actizq.remove(aux);
            }
        }
        actividades = new DualListModel<Actividad>(actizq, actder);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ActividadDialog').show();");
    }
    
    public void enviarActividades(){
        if(selected.getProcesoPorActividadList() != null && !selected.getProcesoPorActividadList().isEmpty()){
            for(Actividad act: actividades.getTarget()){
                if(!selected.getProcesoPorActividadList().contains(act)){
                    ProcesoPorActividad proAct = new ProcesoPorActividad();
                    proAct.setProcCodigo(selected);
                    proAct.setActCodigo(act);
                    selected.getProcesoPorActividadList().add(proAct);
                }
            }   
        }else{
             for(Actividad act: actividades.getTarget()){
                ProcesoPorActividad proAct = new ProcesoPorActividad();
                proAct.setActCodigo(act);
                selected.getProcesoPorActividadList().add(proAct);
            } 
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ActividadDialog').hide();");   
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
    
    @FacesConverter(forClass = Proceso.class)
    public static class ProcesoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProcesoController controller = (ProcesoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "procesoController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Proceso) {
                Proceso o = (Proceso) object;
                return getStringKey(o.getProcCodigo());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Proceso.class.getName()});
                return null;
            }
        }

    }

}

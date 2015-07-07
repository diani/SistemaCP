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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "procesoController")
@SessionScoped
public class ProcesoController implements Serializable {

    @EJB
    private Controladores.ProcesoFacade ejbFacade;
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
    
    private List<Proceso> items = null;
    private Proceso selected; //proceso seleccionado
    private Boolean crear;
    private ProcesoPorActividad selectedPA; // proceso por atividad seleccionado
    private ActividadPorTarea selectedAT; // actividad por tarea seleccionado
    private List<Actividad> actizq = new ArrayList<Actividad>();
    private List<Actividad> actder = new ArrayList<Actividad>();
    private DualListModel<Actividad> actividades = new DualListModel<Actividad>(actizq,actder);
    private List<Producto> productos = null;
    private List<Tarea> tarizq = new ArrayList<Tarea>();
    private List<Tarea> tarder = new ArrayList<Tarea>();
    private DualListModel<Tarea> tareas = new DualListModel<Tarea>(tarizq,tarder);
    private TreeNode arbolProceso;
    
    public ProcesoController() {
    }
    
    public void abrirArbol(){
        selected = getFacade().find(selected.getProcCodigo());
        selected.setProcesoPorActividadList(ejbProcActFacade.buscarListaDeProcesosActivdadPorProceso(selected));
        for(ProcesoPorActividad proActAux : selected.getProcesoPorActividadList()){
            proActAux.setActividadPorTareaListTransient(ejbActTarFacade.buscarListaDeActividadesPorTareaPorProcesoPorActividad(proActAux));
        }
        if(selected.getProdCodigo() != null){
            arbolProceso = new DefaultTreeNode(selected.getProdCodigo().getProdDescripcion(), null);
            if(selected.getProcesoPorActividadList() != null && !selected.getProcesoPorActividadList().isEmpty()){
                TreeNode[] actividadesHijo = new TreeNode[selected.getProcesoPorActividadList().size()];
                for (int i = 0; i < actividadesHijo.length; i++) {
                    actividadesHijo[i]=new DefaultTreeNode(selected.getProcesoPorActividadList().get(i).getActCodigo().getActDescripcion(), arbolProceso);
                    if(selected.getProcesoPorActividadList().get(i).getActividadPorTareaListTransient()!= null && !selected.getProcesoPorActividadList().get(i).getActividadPorTareaListTransient().isEmpty()){
                       TreeNode[] tareasHijo = new TreeNode[selected.getProcesoPorActividadList().get(i).getActividadPorTareaListTransient().size()];
                       for (int y = 0; y < tareasHijo.length; y++) {   
                           tareasHijo[y]=new DefaultTreeNode(selected.getProcesoPorActividadList().get(i).getActividadPorTareaListTransient().get(y).getTarea().getTarDescripcion(), actividadesHijo[i]);
                       }
                    }
                }
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('arbolDialog').show();");
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
        selected = getFacade().find(selected.getProcCodigo());
        selected.setProcesoPorActividadList(ejbProcActFacade.buscarListaDeProcesosActivdadPorProceso(selected));
        for(ProcesoPorActividad proActAux : selected.getProcesoPorActividadList()){
            proActAux.setActividadPorTareaListTransient(ejbActTarFacade.buscarListaDeActividadesPorTareaPorProcesoPorActividad(proActAux));
        }
        crear = false;
        productos = ejbProdFacade.lstProductosHabilitados(true);
        return "/admin/crud/proceso/CrearProceso.xhtml";
    }
    
    public String crearProceso(){
        selected =  new Proceso();
        selected.setProcesoPorActividadList(new ArrayList<ProcesoPorActividad>());
        productos = ejbProdFacade.lstProductosHabilitados(true);
        crear = true;
        return "/admin/crud/proceso/CrearProceso.xhtml";
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProcesoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        selected = new Proceso();
        selectedPA = new ProcesoPorActividad();
        selectedAT = new ActividadPorTarea();
        return "/admin/crud/proceso/List.xhtml";
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
        items = getFacade().findAll();
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    List<ProcesoPorActividad> lstActividadesDelProcesoTemporal = null;
                    
                    //quito la lista de procesos por Actividad de mi proceso seleccionado
                    if(selected.getProcesoPorActividadList() != null && !selected.getProcesoPorActividadList().isEmpty()){
                        lstActividadesDelProcesoTemporal = selected.getProcesoPorActividadList();
                        selected.setProcesoPorActividadList(null);
                    }
                    
                    //guardo mi proceso sin listas
                    if (selected.getProcCodigo() == null) {
                        selected.setProcHabilitadoInterno(true);
                        selected = getFacade().persist(selected); //cuando no existe en la base (crear)
                    }else{
                        selected = getFacade().merge(selected); // cuando ya existe en la base (editar)
                    }
                    
                    
                    //guardo la lista de procesos por actividad
                    if(lstActividadesDelProcesoTemporal != null && !lstActividadesDelProcesoTemporal.isEmpty()){
                        for(ProcesoPorActividad procActiAux : lstActividadesDelProcesoTemporal){
                            procActiAux.setActividadPorTareaList(null);
                            if(procActiAux.getProcActCodigo() == null){
                                ejbProcActFacade.persist(procActiAux);
                            }else{
                                ejbProcActFacade.merge(procActiAux);
                            }
                            
                            //guardar cada elemento de Activad por tarea pertenciente al ProActiAux 
                            if(procActiAux.getActividadPorTareaListTransient() != null && !procActiAux.getActividadPorTareaListTransient().isEmpty()){
                                for(ActividadPorTarea actTar : procActiAux.getActividadPorTareaListTransient()){
                                    Boolean guardar = true;
                                    if(actTar.getActividadPorTareaPK().getProcActCodigo() != 0){
                                        guardar = false;
                                    }
                                    if(guardar){
                                        actTar.getActividadPorTareaPK().setProcActCodigo(procActiAux.getProcActCodigo());
                                        ejbActTarFacade.persist(actTar);
                                    }else{
                                        ejbActTarFacade.merge(actTar);
                                    }
                                }
                            }
                        }
                    }
                    
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
    
    //1 carga la lista de izquierda 
    //si es q ferecha existen cosas traerlas
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
    
    public void abrirTareas(){
        if(selectedPA.getActividadPorTareaListTransient() == null){
            selectedPA.setActividadPorTareaListTransient(new ArrayList<ActividadPorTarea>());
        }
        tarizq = ejbTarFacade.tareashabilitadas(true);
        tarder = new ArrayList<Tarea>();
        for(ActividadPorTarea var: selectedPA.getActividadPorTareaListTransient()){
            tarder.add(var.getTarea());
        }
        for(Tarea aux: tarder){
            if(tarizq.contains(aux)){
                tarizq.remove(aux);
            }
        }
        tareas = new DualListModel<Tarea>(tarizq, tarder);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('TareaDialog').show();");
    }
    
    public void enviarActividades(){
        if(selected.getProcesoPorActividadList() != null && !selected.getProcesoPorActividadList().isEmpty()){
            for(Actividad act: actividades.getTarget()){
                Boolean contiene = false;
                for(ProcesoPorActividad proAct :selected.getProcesoPorActividadList()){
                    if(proAct.getActCodigo().equals(act)){
                       contiene = true;
                    }
                }
                if(!contiene){
                    ProcesoPorActividad proAct = new ProcesoPorActividad();
                    proAct.setProcCodigo(selected);
                    proAct.setActCodigo(act);
                    selected.getProcesoPorActividadList().add(proAct);
                }
                    
            }
        }else{
             for(Actividad act: actividades.getTarget()){
                ProcesoPorActividad proAct = new ProcesoPorActividad();
                proAct.setProcCodigo(selected);
                proAct.setActCodigo(act);
                selected.getProcesoPorActividadList().add(proAct);
            } 
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ActividadDialog').hide();");   
    }

    public void enviarTareas(){
        if(selectedPA.getActividadPorTareaListTransient() != null && !selectedPA.getActividadPorTareaListTransient().isEmpty()){
            for(Tarea tar: tareas.getTarget()){
                Boolean contiene = false;
                for(ActividadPorTarea actTar: selectedPA.getActividadPorTareaListTransient()){
                    if(actTar.getTarea().equals(tar)){
                        contiene = true;
                    }
                }
                if(!contiene){
                    ActividadPorTareaPK actTarPK = new ActividadPorTareaPK();
                    ActividadPorTarea actTar = new ActividadPorTarea();
                    actTar.setProcesoPorActividad(selectedPA);
                    actTar.setTarea(tar);
                    actTarPK.setTarCodigo(tar.getTarCodigo());
                    actTar.setActividadPorTareaPK(actTarPK);
                    selectedPA.getActividadPorTareaListTransient().add(actTar);
                }
            }   
        }else{
             for(Tarea tar: tareas.getTarget()){
                ActividadPorTareaPK actTarPK = new ActividadPorTareaPK();
                ActividadPorTarea actTar = new ActividadPorTarea();
                actTar.setProcesoPorActividad(selectedPA);
                actTar.setTarea(tar);
                actTarPK.setTarCodigo(tar.getTarCodigo());
                actTar.setActividadPorTareaPK(actTarPK);
                selectedPA.getActividadPorTareaListTransient().add(actTar);
            } 
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('TareaDialog').hide();");   
    }
    
    public void eliminarProcAct(){
        if(selectedPA.getProcActCodigo() == null){
            selected.getProcesoPorActividadList().remove(selectedPA);
        }else{
            selected.getProcesoPorActividadList().remove(selectedPA);
            if(selectedPA.getActividadPorTareaListTransient() != null && !selectedPA.getActividadPorTareaListTransient().isEmpty()){
               for(ActividadPorTarea actTarAux : selectedPA.getActividadPorTareaListTransient()){
                   if(actTarAux.getActividadPorTareaPK().getProcActCodigo() != 0){
                       ejbActTarFacade.remove(actTarAux);
                   }
               } 
            }
            ejbProcActFacade.remove(selectedPA);
        }
    }
    public void eliminarActTar(){
        if(selectedAT.getActividadPorTareaPK() == null){
            selectedPA.getActividadPorTareaListTransient().remove(selectedAT);
        }else{
            selectedPA.getActividadPorTareaListTransient().remove(selectedAT);
            ejbActTarFacade.remove(selectedAT);
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
    
      public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
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
    
    
    @FacesConverter("ProcesoControllerConverter")
    public static class ProcesoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0 || value.equals("Seleccionar Uno...")) {
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

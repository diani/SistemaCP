package Controladores;

import Entidades.Producto;
import Controladores.util.JsfUtil;
import Controladores.util.JsfUtil.PersistAction;
import Entidades.Proceso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.application.FacesMessage;
import javax.servlet.ServletContext;

@ManagedBean(name = "productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @EJB
    private Controladores.ProductoFacade ejbFacade;
    @EJB
    private Controladores.ProcesoFacade ejbProcesoFacade;
    private List<Producto> items = null;
    private Producto selected;
    private UploadedFile file;
    
     public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ProductoController() {
    }

    public Producto getSelected() {
        return selected;
    }

    public void setSelected(Producto selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductoFacade getFacade() {
        return ejbFacade;
    }

    public Producto prepareCreate() {
        selected = new Producto();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
   public void uploadImage(String accion) {
        if(file != null && file.getSize() != 0L) {
            try{
                String newFileName = "C:\\Users\\diani\\Documents\\NetBeansProjects\\SistemaCP\\web\\resources\\imagenes\\productos\\" + file.getFileName();;  
                FileOutputStream fos = new FileOutputStream(newFileName);
                InputStream is = file.getInputstream();
                int BUFFER_SIZE = 8192;
                byte[] buffer = new byte[BUFFER_SIZE];
                int a;
                while(true){
                    a = is.read(buffer);
                    if(a < 0) break;
                    fos.write(buffer, 0, a);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch(IOException e){
                FacesMessage message = new FacesMessage("La imagen", file.getFileName() + " No ha sido guardada");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
            if(accion.equals("create")){
                create();
            }else{
                update();    
            }
    }
	
    public List<Producto> getItems() {
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
                    if(file != null && file.getSize() != 0)
                        selected.setProdImagen("/resources/imagenes/productos/"+file.getFileName());
                    getFacade().edit(selected);
                    
                    //selected es un producto seleccionado
                    if(!selected.getProdHabilitado()){
                       List<Proceso> lstProcesoConProducto = ejbProcesoFacade.lstProcesoDeProducto(true, selected);
                       if(lstProcesoConProducto != null && !lstProcesoConProducto.isEmpty()){
                           for(Proceso prolst : lstProcesoConProducto){
                               prolst.setProcHabilitadoInterno(false);
                               ejbProcesoFacade.merge(prolst);
                           }
                       }
                    }else{
                        List<Proceso> lstProcesoConProducto = ejbProcesoFacade.lstProcesoDeProducto(false, selected);
                        if(lstProcesoConProducto != null && !lstProcesoConProducto.isEmpty()){
                           for(Proceso prolst : lstProcesoConProducto){
                               prolst.setProcHabilitadoInterno(true);
                               ejbProcesoFacade.merge(prolst);
                           }
                       }
                    }
                    
                } else {
                    selected.setProdHabilitado(new Boolean(false));
                    getFacade().edit(selected);
                     //selected es un producto seleccionado
                    if(!selected.getProdHabilitado()){
                       List<Proceso> lstProcesoConProducto = ejbProcesoFacade.lstProcesoDeProducto(true, selected);
                       if(lstProcesoConProducto != null && !lstProcesoConProducto.isEmpty()){
                           for(Proceso prolst : lstProcesoConProducto){
                               prolst.setProcHabilitadoInterno(false);
                               ejbProcesoFacade.merge(prolst);
                           }
                       }
                    }else{
                        List<Proceso> lstProcesoConProducto = ejbProcesoFacade.lstProcesoDeProducto(false, selected);
                        if(lstProcesoConProducto != null && !lstProcesoConProducto.isEmpty()){
                           for(Proceso prolst : lstProcesoConProducto){
                               prolst.setProcHabilitadoInterno(true);
                               ejbProcesoFacade.merge(prolst);
                           }
                       }
                    }
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

    public List<Producto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("ProductoControllerConverter")
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getProdCodigo());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }

}

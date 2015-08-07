/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.Cif;
import Entidades.MaterialEmbalaje;
import Entidades.Usuario;
import Entidades.UsuarioPorCif;
import Entidades.UsuarioPorMaterialEmbalaje;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author diani
 */
@ManagedBean(name = "matEmbMensualController")
@SessionScoped
public class matEmbMensualController implements Serializable {

    @EJB
    private Controladores.UsuarioPorMatEmbFacade ejbUsuMatEmbFacade;
    @EJB
    private Controladores.MaterialEmbalajeFacade ejbMatEmbFacade;
    private List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb = null;
    private List<MaterialEmbalaje> lstMatEmb = null;
    private UsuarioPorMaterialEmbalaje selectedUsuMatEmb;
    
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
    
    public void guardarMatEmbMensual(){
        if(selectedUsuMatEmb.getMatEmbCodigo()!= null){
            try{
                if(selectedUsuMatEmb.getUsuMatEmbCodigo()!= null){
                    ejbUsuMatEmbFacade.merge(selectedUsuMatEmb);
                }else
                    ejbUsuMatEmbFacade.persist(selectedUsuMatEmb);
                JsfUtil.addSuccessMessage("Se ha guardado correctamente");
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('MatEmbMenDialog').hide();");
            }catch(Exception ex){
                JsfUtil.addErrorMessage("No se ha podido guardar");
            }
        }else
            JsfUtil.addErrorMessage("Falta datos por ingresar");
            
    }
    
    public void crearMatEmbMensual(Usuario usu){
        selectedUsuMatEmb = new UsuarioPorMaterialEmbalaje();
        selectedUsuMatEmb.setUsuId(usu);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('MatEmbMenDialog').show();");        
    }

    public List<UsuarioPorMaterialEmbalaje> getLstUsuMatEmb() {
        lstUsuMatEmb = ejbUsuMatEmbFacade.lstFecha();
        return lstUsuMatEmb;
    }

    public void setLstUsuMatEmb(List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb) {
        this.lstUsuMatEmb = lstUsuMatEmb;
    }

    public List<MaterialEmbalaje> getLstMatEmb() {
        lstMatEmb = ejbMatEmbFacade.buscarTodosPorHabiltiado(true);
        return lstMatEmb;
    }

    public void setLstMatEmb(List<MaterialEmbalaje> lstMatEmb) {
        this.lstMatEmb = lstMatEmb;
    }

    public UsuarioPorMaterialEmbalaje getSelectedUsuMatEmb() {
        return selectedUsuMatEmb;
    }

    public void setSelectedUsuMatEmb(UsuarioPorMaterialEmbalaje selectedUsuMatEmb) {
        this.selectedUsuMatEmb = selectedUsuMatEmb;
    }
    
    
}

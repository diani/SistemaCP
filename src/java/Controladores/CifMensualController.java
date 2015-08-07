/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.Cif;
import Entidades.Usuario;
import Entidades.UsuarioPorCif;
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
@ManagedBean(name = "cifMensualController")
@SessionScoped
public class CifMensualController implements Serializable {

    @EJB
    private Controladores.UsuarioPorCifFacade ejbUsuCifFacade;
    @EJB
    private Controladores.CifFacade ejbCifFacade;
    private List<UsuarioPorCif> lstUsuCif = null;
    private List<Cif> lstCif = null;
    private UsuarioPorCif selectedUsuCif;

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
    
    public void guardarCifMensual(){
        if(selectedUsuCif.getCifCodigo()!= null){
            try{
                if(selectedUsuCif.getUsuCifCodigo()!= null){
                    ejbUsuCifFacade.merge(selectedUsuCif);
                }else
                    ejbUsuCifFacade.persist(selectedUsuCif);
                JsfUtil.addSuccessMessage("Se ha guardado correctamente");
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('CifMenDialog').hide();");
            }catch(Exception ex){
                JsfUtil.addErrorMessage("No se ha podido guardar");
            }
        }else
            JsfUtil.addErrorMessage("Falta datos por ingresar");
            
    }
    
    public void crearCifMensual(Usuario usu){
        selectedUsuCif = new UsuarioPorCif();
        selectedUsuCif.setUsuId(usu);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('CifMenDialog').show();");        
    }
    
    public List<UsuarioPorCif> getLstUsuCif() {
        lstUsuCif = ejbUsuCifFacade.lstFecha();
        return lstUsuCif;
    }

    public void setLstUsuCif(List<UsuarioPorCif> lstUsuCif) {
        this.lstUsuCif = lstUsuCif;
    }

    public UsuarioPorCif getSelectedUsuCif() {
        return selectedUsuCif;
    }

    public void setSelectedUsuCif(UsuarioPorCif selectedUsuCif) {
        this.selectedUsuCif = selectedUsuCif;
    }

    public List<Cif> getLstCif() {
        lstCif = ejbCifFacade.buscarTodosPorHabiltiado(true);
        return lstCif;
    }

    public void setLstCif(List<Cif> lstCif) {
        this.lstCif = lstCif;
    }
    
 
}

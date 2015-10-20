package Controladores.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }
    
    public static Calendar DateToCalendar(Date date){ 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    public static String convertirFechaAString(Date date){
        String output;
        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        output = formatter.format(date);
        return output;
    }
    
    public static String ObtenerAnio(Date date){
        String output;
        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("yy");
        output = formatter.format(date);
        return output;
    }
    
    
    public static Float DiferenciaFechas(Date dinicio, Date dfinal){
 
           
           long milis1, milis2, diff;
            //INSTANCIA DEL CALENDARIO GREGORIANO
            Calendar cinicio = Calendar.getInstance();
            Calendar cfinal = Calendar.getInstance();
 
            //ESTABLECEMOS LA FECHA DEL CALENDARIO CON EL DATE GENERADO ANTERIORMENTE
             cinicio.setTime(dinicio);
             cfinal.setTime(dfinal);
 
 
         milis1 = cinicio.getTimeInMillis();
 
         milis2 = cfinal.getTimeInMillis();
 
 
         diff = milis2-milis1;
 
 
         // calcular la diferencia en segundos
 
     long diffSegundos =  Math.abs (diff / 1000);
 
 
     // calcular la diferencia en minutos
 
     long diffMinutos =  Math.abs (diff / (60 * 1000));
 
     
     long restominutos = diffMinutos%60;
 
 
 
     // calcular la diferencia en horas
 
     long diffHoras =   (diff / (60 * 60 * 1000));
 
 
 
     // calcular la diferencia en dias
 
     long diffdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );
 
 
     /*
     System.out.println("En segundos: " + diffSegundos + " segundos.");
 
     System.out.println("En minutos: " + diffMinutos + " minutos.");
 
     System.out.println("En horas: " + diffHoras + " horas.");
 
     System.out.println("En dias: " + diffdias + " dias.");
     */
 
     
     
 
            return (float) diffHoras;
    }
}

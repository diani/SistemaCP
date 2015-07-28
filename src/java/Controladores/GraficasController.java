/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author diani
 */
@ManagedBean(name = "graficasController")
@SessionScoped
public class GraficasController implements Serializable {
    @EJB
    private Controladores.TiemposFacade ejbTiemposFacade;
    @EJB
    private Controladores.PlanificacionFacade ejbPlaniProcFacade;
    @EJB
    private Controladores.UsuarioFacade ejbUsuarioFacade;
    
    private List<TiemposProduccion> lstTiempos = null;
    private List<PlanificacionProcesos> lstPlaniproc = null;
    private PieChartModel livePieModel= new PieChartModel();   
    
    private List<TiemposProduccion> lstUsuariosTrabajando = null;
    private List<Usuario> lstUsuarios = null;
    private PieChartModel livePieModelUsuarios = new PieChartModel();
    
    private List<Usuario> lstUsuariosActivos = null;
    
    private String texto;
  
    public PieChartModel getLivePieModel() {
        lstPlaniproc = ejbPlaniProcFacade.buscarFechaIni();
        int total = lstPlaniproc.size();
        int activos = 0;
        
        for(PlanificacionProcesos aux :lstPlaniproc){
            List<TiemposProduccion> lsttiempoaux = ejbTiemposFacade.tiempoPorPlanificacion(aux);
            if(lsttiempoaux != null && !lsttiempoaux.isEmpty()){
                activos++;
            }
        }
        int inactivos=total-activos;
        
        livePieModel.setDataFormat("value");
        livePieModel.setShowDataLabels(true);
        
        livePieModel.getData().put("Inactivos", inactivos);
        livePieModel.getData().put("activos", activos);
         
        livePieModel.setTitle("Productos que se están realizando");
        livePieModel.setLegendPosition("ne");
         
        livePieModel.setSeriesColors("336B5F,3A0F97");
        return livePieModel;
    }
    
    public List<Usuario> usuariosTrabajando(PlanificacionProcesos plpr){
        List<TiemposProduccion> tipro = ejbTiemposFacade.tiempoPorPlanificacion(plpr);
        List<Usuario> us=new ArrayList<Usuario>();
        if(tipro!=null && !tipro.isEmpty()){
            for(TiemposProduccion tp : tipro){
                if(!us.contains(tp.getUsuId())){
                    us.add(tp.getUsuId());
                }
            }
        }
        return us;
    }
    
    public PieChartModel getLivePieModelUsuarios() {
        lstPlaniproc = ejbPlaniProcFacade.buscarFechaIni();
        
        if(lstUsuarios == null || lstUsuarios.isEmpty()){
            lstUsuarios = ejbUsuarioFacade.findByHabilitado(Boolean.TRUE);
        }
        
        int total = lstUsuarios.size();
        int activos = 0;
        List<Usuario> lstTempUsu = new ArrayList<Usuario>();
        lstUsuariosActivos = new ArrayList<Usuario>();
        for(PlanificacionProcesos aux :lstPlaniproc){
            lstTempUsu.addAll(usuariosTrabajando(aux));
        }
        for(Usuario temp : lstTempUsu){
            if(!lstUsuariosActivos.contains(temp)){
                lstUsuariosActivos.add(temp);
            }
        }
        activos =lstUsuariosActivos.size();
        int inactivos=total-activos;
        
        livePieModelUsuarios.getData().put("Inactivos", inactivos);
        livePieModelUsuarios.getData().put("activos", activos);
        
        livePieModelUsuarios.setTitle("Personas Trabajando");
        livePieModelUsuarios.setLegendPosition("ne");
        livePieModelUsuarios.setShowDataLabels(true);
        livePieModelUsuarios.setDataFormat("value");
        
        return livePieModelUsuarios;
    }
    
    public void itemSelect(ItemSelectEvent event) {
        if(event.getItemIndex() == 1){    
            texto = "Productos Activos: ";
            for(PlanificacionProcesos aux :lstPlaniproc){
                List<TiemposProduccion> lsttiempoaux = ejbTiemposFacade.tiempoPorPlanificacion(aux);
                if(lsttiempoaux != null && !lsttiempoaux.isEmpty()){
                    texto += aux.getProcCodigo().getProdCodigo().getProdDescripcion() + ", ";
                }
            }
             texto =texto.substring(0,texto.length() - 2);
        }else{
            texto = "Productos Inactivos: ";
            for(PlanificacionProcesos aux :lstPlaniproc){
                List<TiemposProduccion> lsttiempoaux = ejbTiemposFacade.tiempoPorPlanificacion(aux);
                if(lsttiempoaux == null || lsttiempoaux.isEmpty()){
                    texto += aux.getProcCodigo().getProdCodigo().getProdDescripcion() + ", ";
                }
            }
            texto = texto.substring(0,texto.length() - 2);
        }
    }
    
    public void itemSelectUsuarios(ItemSelectEvent event) {
        if(event.getItemIndex() == 1){    
            texto = "Usuarios Trabajando: ";
            for(Usuario aux :lstUsuariosActivos){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto =texto.substring(0,texto.length() - 2);
        }else{
            texto = "Usuarios Inactivos: ";
            List<Usuario> lstTemp = lstUsuarios;
            for(Usuario aux :lstUsuariosActivos){
                if(lstTemp.contains(aux))
                    lstTemp.remove(aux);
            }
            for(Usuario aux :lstTemp){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto = texto.substring(0,texto.length() - 2);
        }
    }
    
    public GraficasController() {
    }

    public List<TiemposProduccion> getLstTiempos() {
        return lstTiempos;
    }

    public void setLstTiempos(List<TiemposProduccion> lstTiempos) {
        this.lstTiempos = lstTiempos;
    }

    public List<PlanificacionProcesos> getLstPlaniproc() {
        return lstPlaniproc;
    }

    public void setLstPlaniproc(List<PlanificacionProcesos> lstPlaniproc) {
        this.lstPlaniproc = lstPlaniproc;
    }

    public List<TiemposProduccion> getLstUsuariosTrabajando() {
        return lstUsuariosTrabajando;
    }

    public void setLstUsuariosTrabajando(List<TiemposProduccion> lstUsuariosTrabajando) {
        this.lstUsuariosTrabajando = lstUsuariosTrabajando;
    }

    public List<Usuario> getLstUsuarios() {
        return lstUsuarios;
    }

    public void setLstUsuarios(List<Usuario> lstUsuarios) {
        this.lstUsuarios = lstUsuarios;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<Usuario> getLstUsuariosActivos() {
        return lstUsuariosActivos;
    }

    public void setLstUsuariosActivos(List<Usuario> lstUsuariosActivos) {
        this.lstUsuariosActivos = lstUsuariosActivos;
    }
    
    
    
}

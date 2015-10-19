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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
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
    private List<Usuario> lstUsuariosFaltantes = null;
    private List<Usuario> lstUsuariosAlmorzando = null;
    private List<Usuario> lstUsuariosFinalizan = null;
    
    private String texto;
    
    private BarChartModel animatedModel2;
    
    @PostConstruct
    public void init() {
        createAnimatedModels();
    }
  
    private void createAnimatedModels() {
         
        animatedModel2 = initBarModel();
        animatedModel2.setTitle("Actividades del dia de hoy");
        animatedModel2.setAnimate(true);
        animatedModel2.setLegendPosition("ne");
        Axis yAxis = animatedModel2.getAxis(AxisType.Y);
        yAxis.setMin(0);
    }
    
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries cantidadMP = new ChartSeries();
        
        cantidadMP.setLabel("Cantidad MP");
        List<PlanificacionProcesos> planiproc;
        planiproc = ejbPlaniProcFacade.buscarFechaIni();
        
        for(PlanificacionProcesos pla : planiproc){
            try{
                cantidadMP.set(pla.getProcCodigo().getProdCodigo().getProdDescripcion(), pla.getPlaProcMateriaPrima());
            }catch(Exception e){
                cantidadMP.set(pla.getProcCodigo().getProdCodigo().getProdDescripcion(), 0);
            }
            
        }
        
        model.addSeries(cantidadMP);
         
        return model;
    }
    
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
        if(lstUsuarios == null || lstUsuarios.isEmpty()){
            lstUsuarios = ejbUsuarioFacade.findByHabilitado(Boolean.TRUE);
        }
        Calendar calStart = new GregorianCalendar();
        calStart.setTime(new Date());
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        Date midnightYesterday = calStart.getTime();
        int total = lstUsuarios.size();
        int activos = 0;
        int almorzando = 0;
        int noVino=0;
        int acabaron=0;
        
        List<Usuario> lstTempUsu = new ArrayList<Usuario>();
        
        lstUsuariosActivos = new ArrayList<Usuario>();
        lstUsuariosAlmorzando= new ArrayList<Usuario>();
        lstUsuariosFaltantes = new ArrayList<Usuario>();
        lstUsuariosFinalizan = new ArrayList<Usuario>();
        
        for(Usuario usu: lstUsuarios){
            TiemposProduccion ultimoTiempo = ejbTiemposFacade.ultimoTiempoPorUsuarioYdia(usu);
            
            
            if(ultimoTiempo != null){
                
                if((ultimoTiempo.getTieProdHoraIni()!=null && ultimoTiempo.getTieProdHoraIni().compareTo(midnightYesterday)==-1)){
                    ultimoTiempo = null;
                }
                else if((ultimoTiempo.getTieProdHoraFin()!=null && ultimoTiempo.getTieProdHoraFin().compareTo(midnightYesterday)==-1)){
                    ultimoTiempo = null;
                }
            }
            if(ultimoTiempo==null){
                lstUsuariosFaltantes.add(usu);
            }else if(ultimoTiempo.getTieProdHoraFin()!=null && ultimoTiempo.getTieProdFinal()){ //true cuando es positivo y 1 en la bdd
                lstUsuariosFinalizan.add(usu);
            }else if(ultimoTiempo.getTieProdHoraFin()!=null && !ultimoTiempo.getTieProdFinal()){// !(false) cuando es negativo y 0 en la bdd
                lstUsuariosAlmorzando.add(usu);
            }else 
                lstUsuariosActivos.add(usu);                            
        }
        
        activos=lstUsuariosActivos.size();
        almorzando=lstUsuariosAlmorzando.size();
        noVino=lstUsuariosFaltantes.size();
        acabaron=lstUsuariosFinalizan.size();
        
        livePieModelUsuarios.getData().put("Trabajando", activos);
        livePieModelUsuarios.getData().put("Personas en almuerzo", almorzando);
        livePieModelUsuarios.getData().put("Finalizaron", acabaron);
        livePieModelUsuarios.getData().put("Ausentes", noVino);
        
        
        livePieModelUsuarios.setTitle("Personal");
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
        if(event.getItemIndex() == 0){    
            texto = "Personal Trabajando: ";
            for(Usuario aux :lstUsuariosActivos){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto =texto.substring(0,texto.length() - 2);
        }else if(event.getItemIndex() == 1){
            texto = "Personal Almorzando: ";
            for(Usuario aux :lstUsuariosAlmorzando){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto =texto.substring(0,texto.length() - 2);
        }else if(event.getItemIndex() == 2){
            texto = "Finalizo Jornada: ";
            for(Usuario aux :lstUsuariosFinalizan){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto =texto.substring(0,texto.length() - 2);
        }else if(event.getItemIndex() == 3){
            texto = "Personal Ausente: ";
            for(Usuario aux :lstUsuariosFaltantes){
                texto += aux.getUsuNombre() + " " + aux.getUsuApellido() + ", ";
            }
            texto =texto.substring(0,texto.length() - 2);
        }
    }
    
    public GraficasController() {
    }

    public List<Usuario> getLstUsuariosFaltantes() {
        return lstUsuariosFaltantes;
    }

    public void setLstUsuariosFaltantes(List<Usuario> lstUsuariosFaltantes) {
        this.lstUsuariosFaltantes = lstUsuariosFaltantes;
    }

    public List<Usuario> getLstUsuariosAlmorzando() {
        return lstUsuariosAlmorzando;
    }

    public void setLstUsuariosAlmorzando(List<Usuario> lstUsuariosAlmorzando) {
        this.lstUsuariosAlmorzando = lstUsuariosAlmorzando;
    }

    public List<Usuario> getLstUsuariosFinalizan() {
        return lstUsuariosFinalizan;
    }

    public void setLstUsuariosFinalizan(List<Usuario> lstUsuariosFinalizan) {
        this.lstUsuariosFinalizan = lstUsuariosFinalizan;
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

    public BarChartModel getAnimatedModel2() {
        return animatedModel2;
    }

    public void setAnimatedModel2(BarChartModel animatedModel2) {
        this.animatedModel2 = animatedModel2;
    }
    
    
    
}

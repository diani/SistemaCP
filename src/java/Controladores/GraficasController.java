/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
    private List<TiemposProduccion> lstTiempos = null;
    private List<PlanificacionProcesos> lstPlaniproc = null;
    private PieChartModel livePieModel = new PieChartModel();
 
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
        
        livePieModel.getData().put("Inactivos", inactivos);
        livePieModel.getData().put("activos", activos);
        
        livePieModel.setTitle("Productos que se están reaizando");
        livePieModel.setLegendPosition("ne");
        livePieModel.setShowDataLabels(true);
        
        return livePieModel;
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
    
    
}

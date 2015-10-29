/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.Cif;
import Entidades.MaterialEmbalaje;
import Entidades.ProduccionDiaria;
import Entidades.ProduccionPorPresentacion;
import Entidades.Producto;
import Entidades.UsuarioPorCif;
import Entidades.UsuarioPorMaterialEmbalaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author diani
 */
@ManagedBean(name = "variacionCostosController")
@SessionScoped
public class VariacionCostosController implements Serializable{
 
    @EJB
    private Controladores.UsuarioPorCifFacade ejbUsuCifFacade;
    @EJB
    private Controladores.CifFacade ejbCifFacade;
    @EJB
    private Controladores.UsuarioPorMatEmbFacade ejbUsuMatEmbFacade;
    @EJB
    private Controladores.ProductoFacade ejbProdFacade;
    @EJB
    private Controladores.MaterialEmbalajeFacade ejbMEFacade;
    @EJB
    private Controladores.ProduccionDiariaFacade ejbProduDiarFacade;
    @EJB
    private Controladores.ProduccionPorPresentacionFacade ejbProduccionPreFacade;
    private List<UsuarioPorCif> lstUsuCif = null;
    private List<Cif> lstCif = null;
    private List<Producto> lstProd = null;
    private List<ProduccionDiaria> lstProduDia = null;
    private List<MaterialEmbalaje> lstME = null;
    private List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb = null;
    private List<UsuarioPorMaterialEmbalaje> lstMP = null;
    private List<UsuarioPorMaterialEmbalaje> lstMatE = null;
    private Date fechaIni;
    private Date fechaFin = new Date();
    private Boolean generado=false;
    private String console;
    private LineChartModel animatedModel;
    
    public void generarReporte(){
        
        if(console.contains("CIF")){
            calcularCIF(); 
        }else if(console.contains("MP")){
            calcularMP();
        }else if(console.contains("Material de Embalaje")){
            calcularME();
        }else if(console.contains("Ingresos")){
            calcularIngresos();
        }
        createAnimatedModels();
        //JsfUtil.addSuccessMessage(""+lstProduDia.size());
        generado=true;       
    }
    
    public void calcularCIF(){
        lstUsuCif = ejbUsuCifFacade.lstCifs(fechaIni, fechaFin);
        lstCif = ejbCifFacade.buscarTodosPorHabiltiado(true);
    }
    
    public void calcularIngresos(){
        lstProduDia = ejbProduDiarFacade.lstProduDia(fechaIni, fechaFin);
        lstProd = ejbProdFacade.lstProductosHabilitados(true);
        for(ProduccionDiaria tot :lstProduDia){
            tot.setProduccionPorPresentacionList(ejbProduccionPreFacade.buscarProPorPrePorParamProdu(tot));
            tot.setTotalProdT(0F);
            for(ProduccionPorPresentacion cantTot :tot.getProduccionPorPresentacionList()){
                tot.setTotalProdT((tot.getTotalProdT() != null ? tot.getTotalProdT():0F)+(cantTot.getProdPorPresCantPt()!= null ? cantTot.getProdPorPresCantPt() : 0F)*cantTot.getPresentacionProducto().getPreProdEquivalenciaPt());
            }
        }
    }
    
    public void calcularMP(){
        lstUsuMatEmb = ejbUsuMatEmbFacade.lstUsuMatEmb(fechaIni, fechaFin);
        lstProd = ejbProdFacade.lstProductosHabilitados(true);
        lstMP = new ArrayList<UsuarioPorMaterialEmbalaje>();
        for(UsuarioPorMaterialEmbalaje usuMatEmb: lstUsuMatEmb){
            for(Producto produ: lstProd){
                if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains(produ.getProdDescripcion())){
                    lstMP.add(usuMatEmb);
                }
            }
        }
    }
    
    public void calcularME(){
        lstUsuMatEmb = ejbUsuMatEmbFacade.lstUsuMatEmb(fechaIni, fechaFin);
        lstME = ejbMEFacade.buscarTodosPorHabiltiado(true);
        lstMatE = new ArrayList<UsuarioPorMaterialEmbalaje>();
        for(UsuarioPorMaterialEmbalaje usuMatEmb: lstUsuMatEmb){
            if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación KL")){
                    lstMatE.add(usuMatEmb);
                }else if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación 500 GR")){
                    lstMatE.add(usuMatEmb);
                }else if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación 150 GR")){
                    lstMatE.add(usuMatEmb);
                }else if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación 110 GR")){
                    lstMatE.add(usuMatEmb);
                }else if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación Granel")){
                    lstMatE.add(usuMatEmb);
                }
        }
    }
    
    private void createAnimatedModels() {
        
        if(console.equals("CIF")){
            animatedModel = cifLinearModel();
            animatedModel.setTitle("CIF");
            animatedModel.setAnimate(true);
            animatedModel.setLegendPosition("se");
            animatedModel.getAxes().put(AxisType.X, new CategoryAxis("Fecha"));
            Axis yAxis = animatedModel.getAxis(AxisType.Y);
            yAxis.setMin(0); 
        }else if(console.equals("MP")){
            animatedModel = mpLinearModel();
            animatedModel.setTitle("MP");
            animatedModel.setAnimate(true);
            animatedModel.setLegendPosition("se");
            animatedModel.getAxes().put(AxisType.X, new CategoryAxis("Fecha"));
            Axis yAxis = animatedModel.getAxis(AxisType.Y);
            yAxis.setMin(0); 
        }else if(console.equals("Material de Embalaje")){
            animatedModel = meLinearModel();
            animatedModel.setTitle("Material de Embalaje");
            animatedModel.setAnimate(true);
            animatedModel.setLegendPosition("se");
            animatedModel.getAxes().put(AxisType.X, new CategoryAxis("Fecha"));
            Axis yAxis = animatedModel.getAxis(AxisType.Y);
            yAxis.setMin(0); 
        }else if(console.equals("Ingresos")){
            animatedModel = ingresosLinearModel();
            animatedModel.setTitle("Ingresos");
            animatedModel.setAnimate(true);
            animatedModel.setLegendPosition("se");
            animatedModel.getAxes().put(AxisType.X, new CategoryAxis("Fecha"));
            Axis yAxis = animatedModel.getAxis(AxisType.Y);
            yAxis.setMin(0); 
        }
        
         
    }
    
    private LineChartModel cifLinearModel() {
        LineChartModel model = new LineChartModel();
        for(Cif ciif: lstCif){
            LineChartSeries series1 = new LineChartSeries();  //series 1 es una linea del grafico
            series1.setLabel(ciif.getCifDescripcion());
            for(UsuarioPorCif usucif: lstUsuCif){
                if(usucif.getCifCodigo().getCifDescripcion().equals(ciif.getCifDescripcion()) ){
                    series1.set(JsfUtil.convertirFechaAString(usucif.getUsuCifFecha()), usucif.getUsuCifCosto());
                }               
            }
            model.addSeries(series1);
        }
        return model;
    }
    
    private LineChartModel mpLinearModel() {
        LineChartModel model = new LineChartModel();
        for(Producto produ: lstProd){
            if(!produ.getProdAccion()){
                LineChartSeries series1 = new LineChartSeries();  //series 1 es una linea del grafico
                series1.setLabel(produ.getProdDescripcion());
                for(UsuarioPorMaterialEmbalaje usume: lstMP){
                    if(usume.getMatEmbCodigo().getMatEmbDescripcion().contains(produ.getProdDescripcion()) ){
                        series1.set(JsfUtil.convertirFechaAString(usume.getUsuMatEmbFecha()), usume.getUsuMatEmbCostoUni());
                    }               
                }
                model.addSeries(series1);
            }
        }
        return model;
    }
    
    private LineChartModel meLinearModel() {
        LineChartModel model = new LineChartModel();
        for(MaterialEmbalaje me: lstME){
            if(me.getMatEmbDescripcion().contains("Presentación KL") || me.getMatEmbDescripcion().contains("Presentación 500 GR") || me.getMatEmbDescripcion().contains("Presentación 150 GR") || me.getMatEmbDescripcion().contains("Presentación 110 GR") || me.getMatEmbDescripcion().contains("Presentación Granel") ){    
                LineChartSeries series1 = new LineChartSeries();  //series 1 es una linea del grafico
                series1.setLabel(me.getMatEmbDescripcion());
                for(UsuarioPorMaterialEmbalaje usume: lstMatE){
                    if(usume.getMatEmbCodigo().getMatEmbDescripcion().contains(me.getMatEmbDescripcion()) ){
                        series1.set(JsfUtil.convertirFechaAString(usume.getUsuMatEmbFecha()), usume.getUsuMatEmbCostoUni());
                    }               
                }
                model.addSeries(series1);
            }
        }
        return model;
    }
    
    private LineChartModel ingresosLinearModel() {
        LineChartModel model = new LineChartModel();
        for(Producto produ: lstProd){
            if(!produ.getProdAccion()){
                LineChartSeries series1 = new LineChartSeries();  //series 1 es una linea del grafico
                series1.setLabel(produ.getProdDescripcion());
                for(ProduccionDiaria proddia: lstProduDia){
                    if(proddia.getProdCodigo().getProdDescripcion().contains(produ.getProdDescripcion()) ){
                        series1.set(JsfUtil.convertirFechaAString(proddia.getProdDiaFecha()), proddia.getTotalProdT());
                    }               
                }
                model.addSeries(series1);
            }
        }
        return model;
    }
    
    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getGenerado() {
        return generado;
    }

    public void setGenerado(Boolean generado) {
        this.generado = generado;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public List<UsuarioPorCif> getLstUsuCif() {
        return lstUsuCif;
    }

    public void setLstUsuCif(List<UsuarioPorCif> lstUsuCif) {
        this.lstUsuCif = lstUsuCif;
    }

    public LineChartModel getAnimatedModel() {
        return animatedModel;
    }

    public void setAnimatedModel(LineChartModel animatedModel) {
        this.animatedModel = animatedModel;
    }

    public List<Cif> getLstCif() {
        return lstCif;
    }

    public void setLstCif(List<Cif> lstCif) {
        this.lstCif = lstCif;
    }

    public List<Producto> getLstProd() {
        return lstProd;
    }

    public void setLstProd(List<Producto> lstProd) {
        this.lstProd = lstProd;
    }

    public List<UsuarioPorMaterialEmbalaje> getLstUsuMatEmb() {
        return lstUsuMatEmb;
    }

    public void setLstUsuMatEmb(List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb) {
        this.lstUsuMatEmb = lstUsuMatEmb;
    }

    public List<UsuarioPorMaterialEmbalaje> getLstMP() {
        return lstMP;
    }

    public void setLstMP(List<UsuarioPorMaterialEmbalaje> lstMP) {
        this.lstMP = lstMP;
    }

    public List<MaterialEmbalaje> getLstME() {
        return lstME;
    }

    public void setLstME(List<MaterialEmbalaje> lstME) {
        this.lstME = lstME;
    }

    public List<UsuarioPorMaterialEmbalaje> getLstMatE() {
        return lstMatE;
    }

    public void setLstMatE(List<UsuarioPorMaterialEmbalaje> lstMatE) {
        this.lstMatE = lstMatE;
    }

    public List<ProduccionDiaria> getLstProduDia() {
        return lstProduDia;
    }

    public void setLstProduDia(List<ProduccionDiaria> lstProduDia) {
        this.lstProduDia = lstProduDia;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.IngresosEstructura;
import Entidades.MODEstructura;
import Entidades.MaterialEmbalaje;
import Entidades.PresentacionProducto;
import Entidades.ProduccionDiaria;
import Entidades.ProduccionPorPresentacion;
import Entidades.Producto;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import Entidades.UsuarioPorCif;
import Entidades.UsuarioPorMaterialEmbalaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author diani
 */
@ManagedBean(name = "balanceController")
@SessionScoped
public class BalanceController implements Serializable{
    
    @EJB
    private Controladores.UsuarioPorCifFacade ejbUsuCifFacade;
    @EJB
    private Controladores.ProduccionDiariaFacade ejbProduDiaFacade;
    @EJB
    private Controladores.UsuarioPorMatEmbFacade ejbUsuMatEmbFacade;
    @EJB
    private Controladores.UsuarioFacade ejbUsuFacade;
    @EJB
    private Controladores.TiemposFacade ejbTiemFacade;
    @EJB
    private Controladores.PresentacionProductoFacade ejbPreProdFacade;
    private Date fechaIni;
    private Date fechaFin = new Date();
    private List<UsuarioPorCif> lstUsuCif = null;
    private List<ProduccionDiaria> lstProduDia = null;
    private List<Producto> lstProd = null;
    private List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb = null;
    private List<Usuario> lstUsu = null;
    private List<MODEstructura> lstModEst = null;
    private List<PresentacionProducto> lstPreProd = null;
    private List<IngresosEstructura> lstIngEst = null;
    private Float sumaCif;
    private Float totalMP;
    private Float totalMOD;
    private Float totalIngresos;
    private Float totalME;
    private Boolean generado=false;
    private Float totalEgresos;
    
    private PieChartModel pieModel;
    
    public void BalanceController(){
        generado=false;
        fechaIni = null;
    }
    
    private void crearPieModel() {
        pieModel = new PieChartModel();
        totalEgresos = sumaCif+totalME+totalMOD+totalMP;
        
        pieModel.set("Ingresos", totalIngresos);
        pieModel.set("Egresos", totalEgresos);
        
        pieModel.setTitle("Costos");
        pieModel.setLegendPosition("w");
        pieModel.setShowDataLabels(true);
        pieModel.setDataFormat("value");
    }
    
    public void generarReporte(){
        calcularCIF();
        calcularProduccionDiaria();
        calcularMP();
        calcularMOD();
        calcularIngresos();
        calcularME();
        crearPieModel();
        JsfUtil.addSuccessMessage(""+lstProduDia.size());
        generado=true;
        
    }
    
    public void calcularCIF(){
        lstUsuCif = ejbUsuCifFacade.lstCifs(fechaIni, fechaFin);
        sumaCif=0F;
        for(UsuarioPorCif usucif : lstUsuCif){
            sumaCif+= usucif.getUsuCifCosto();
        }
    }
    
    public void calcularProduccionDiaria(){
        lstProduDia = ejbProduDiaFacade.lstProduDia(fechaIni, fechaFin);
        lstProd = new ArrayList<Producto>();
        for(ProduccionDiaria proddia: lstProduDia){
            if(!lstProd.contains(proddia.getProdCodigo())){
                proddia.getProdCodigo().setCantMP(proddia.getProdDiaCantMp());
                lstProd.add(proddia.getProdCodigo());                
            }else{
                Producto produc =  lstProd.get(lstProd.indexOf(proddia.getProdCodigo()));
                lstProd.remove(produc);
                produc.setCantMP(produc.getCantMP()+proddia.getProdDiaCantMp());
                lstProd.add(produc);
            }
                
        }  
    }
    
    public void calcularMP(){
        lstUsuMatEmb = ejbUsuMatEmbFacade.lstUsuMatEmb(fechaIni, fechaFin);
        for(UsuarioPorMaterialEmbalaje usuMatEmb: lstUsuMatEmb){
            for(Producto produ: lstProd){
                if(usuMatEmb.getMatEmbCodigo().getMatEmbDescripcion().contains(produ.getProdDescripcion())){
                    produ.setCostoMP(usuMatEmb.getUsuMatEmbCostoUni());
                }
            }
        }
        totalMP=0F;
        for(Producto product: lstProd){
            if(product.getCostoMP()!= null)
                totalMP+= product.getCostoMP()*product.getCantMP();
        }
    }
    
    public void calcularMOD(){
        lstModEst= new ArrayList<MODEstructura>();
        lstUsu = ejbUsuFacade.findByHabilitado(true);
        for(Usuario usuu: lstUsu){   //se llena usuario con su valor de hora
            MODEstructura mod=new MODEstructura();
            mod.setEmpleado(usuu);   //pasar usuario a estructura
            if(usuu.getRolId().getRolDescripcion().equals("JEFE")){
                mod.setValorHora(5F);  //si gana 800 se divide para 160(num hrs mensuals q se trabaja) por lo tanto 5
            }else  if(usuu.getRolId().getRolDescripcion().equals("OPERARIO")){
                mod.setValorHora(2.21F); //si gana 354
            }else  if(usuu.getRolId().getRolDescripcion().equals("ADMINISTRADOR")){
                mod.setValorHora(7.5F); //si gana 1200
            }    
            lstModEst.add(mod);
        }
        
        for(MODEstructura mood: lstModEst){  //listado d horas d un usuario
            List<TiemposProduccion> lstTiempos  = new ArrayList<TiemposProduccion>();
            lstTiempos = ejbTiemFacade.lstTiemUsuFecha(mood.getEmpleado(), fechaIni, fechaFin);
            mood.setCantHorasTrabajadas(0F);
            mood.setValorHorasExtra(0F);
            mood.setSueldoTotal(0F);
            if(lstTiempos != null && !lstTiempos.isEmpty()){
                Float cantHrs=0F;
                Date horaIni=null;
                Date horaFin=null;
                for( TiemposProduccion elemLstTie : lstTiempos){  //lsttiempos tienen las 4 horas registradas diarias
                   if(elemLstTie.getTieProdHoraIni()!=null){
                       horaIni = elemLstTie.getTieProdHoraIni();
                   }else if(elemLstTie.getTieProdHoraFin()!=null){
                       horaFin = elemLstTie.getTieProdHoraFin();
                   }
                   if(horaIni!=null && horaFin!=null){
                       cantHrs += JsfUtil.DiferenciaFechas(horaIni, horaFin);
                       horaIni=null;
                       horaFin=null;
                   }
                }
                mood.setCantHorasTrabajadas(cantHrs);
                mood.setSueldoTotal(mood.getValorHora()*mood.getCantHorasTrabajadas());
                if(mood.getEmpleado().getRolId().getRolDescripcion().equals("ADMINISTRADOR")){
                    Float pagoExtra=0F;
                    pagoExtra = mood.getSueldoTotal() - 1200F;
                    if(pagoExtra<0F){
                        pagoExtra=0F;
                    }
                    mood.setValorHorasExtra(pagoExtra);
                }
                else if(mood.getEmpleado().getRolId().getRolDescripcion().equals("JEFE")){
                    Float pagoExtra=0F;
                    pagoExtra = mood.getSueldoTotal() - 800F;
                    if(pagoExtra<0F){
                        pagoExtra=0F;
                    }
                    mood.setValorHorasExtra(pagoExtra);
                }
                else if(mood.getEmpleado().getRolId().getRolDescripcion().equals("OPERARIO")){
                    Float pagoExtra=0F;
                    pagoExtra = mood.getSueldoTotal() - 354F;
                    if(pagoExtra<0F){
                        pagoExtra=0F;
                    }
                    mood.setValorHorasExtra(pagoExtra);
                }
            }
        }
        totalMOD=0F;
        for(MODEstructura mood1: lstModEst){
            if(mood1.getSueldoTotal()!=null)
            totalMOD += mood1.getSueldoTotal();
        }
        
    }
    
    public void calcularIngresos(){
        lstPreProd = ejbPreProdFacade.preseProdHabilitadas(true);
        lstIngEst = new ArrayList<IngresosEstructura>();
        for(PresentacionProducto preprod: lstPreProd){
            IngresosEstructura ingestr = new IngresosEstructura();
            ingestr.setPreprod(preprod);
            if(preprod.getPreProdDescripcion().equals("KL")){
                ingestr.setPrecio(3.50F);
            }else if(preprod.getPreProdDescripcion().equals("500 GR")){
                ingestr.setPrecio(2.60F);
            }else if(preprod.getPreProdDescripcion().equals("150 GR")){
                ingestr.setPrecio(1.40F);
            }else if(preprod.getPreProdDescripcion().equals("110 GR")){
                ingestr.setPrecio(1.20F);
            }else if(preprod.getPreProdDescripcion().equals("Granel")){
                ingestr.setPrecio(1.10F);
            }
            
            lstIngEst.add(ingestr);
        }
        Float contKl = 0F;
        Float cont500gr = 0F;
        Float cont150gr = 0F;
        Float cont110gr = 0F;
        Float contGranel = 0F;
        for(ProduccionDiaria prepro: lstProduDia){
            for(ProduccionPorPresentacion prodPre: prepro.getProduccionPorPresentacionList()){
                if(prodPre.getPresentacionProducto().getPreProdDescripcion().equals("KL")){
                    if(prodPre.getProdPorPresCantPt() != null)
                        contKl += prodPre.getProdPorPresCantPt();
                }else if(prodPre.getPresentacionProducto().getPreProdDescripcion().equals("500 GR")){
                    if(prodPre.getProdPorPresCantPt() != null)
                        cont500gr += prodPre.getProdPorPresCantPt();
                }else if(prodPre.getPresentacionProducto().getPreProdDescripcion().equals("150 GR")){
                    if(prodPre.getProdPorPresCantPt() != null)
                        cont150gr += prodPre.getProdPorPresCantPt();
                }else if(prodPre.getPresentacionProducto().getPreProdDescripcion().equals("110 GR")){
                    if(prodPre.getProdPorPresCantPt() != null)
                        cont110gr += prodPre.getProdPorPresCantPt();
                }else if(prodPre.getPresentacionProducto().getPreProdDescripcion().equals("Granel")){
                    if(prodPre.getProdPorPresCantPt() != null)
                        contGranel += prodPre.getProdPorPresCantPt();
                }
            }
        }
        
        for(IngresosEstructura ingEst: lstIngEst){
            if(ingEst.getPreprod().getPreProdDescripcion().equals("KL")){
                ingEst.setCant(contKl);
            }else if(ingEst.getPreprod().getPreProdDescripcion().equals("500 GR")){
                ingEst.setCant(cont500gr);
            }else if(ingEst.getPreprod().getPreProdDescripcion().equals("150 GR")){
                ingEst.setCant(cont150gr);
            }else if(ingEst.getPreprod().getPreProdDescripcion().equals("110 GR")){
                ingEst.setCant(cont110gr);
            }else if(ingEst.getPreprod().getPreProdDescripcion().equals("Granel")){
                ingEst.setCant(contGranel);
            }
        }
        
        totalIngresos=0F;
        for(IngresosEstructura inEs: lstIngEst){
            if(inEs.getCant()!= null){
                totalIngresos+=(inEs.getPrecio() * inEs.getCant());
            }
        }
        
    }
    
    public void calcularME(){
        Float costoKl=0F;
        Float costo500gr=0F;
        Float costo150gr=0F;
        Float costo110gr=0F;
        Float costoGranel=0F;
        for(UsuarioPorMaterialEmbalaje usuMatEm: lstUsuMatEmb){
            if(usuMatEm.getMatEmbCodigo().getMatEmbDescripcion().contains("Presentación KL")){
                costoKl+=usuMatEm.getUsuMatEmbCostoUni();
            }else if(usuMatEm.getMatEmbCodigo().getMatEmbDescripcion().contains("500 GR")){
                costo500gr+=usuMatEm.getUsuMatEmbCostoUni();
            }else if(usuMatEm.getMatEmbCodigo().getMatEmbDescripcion().contains("150 GR")){
                costo150gr+=usuMatEm.getUsuMatEmbCostoUni();
            }else if(usuMatEm.getMatEmbCodigo().getMatEmbDescripcion().contains("110 GR")){
                costo110gr+=usuMatEm.getUsuMatEmbCostoUni();
            }else if(usuMatEm.getMatEmbCodigo().getMatEmbDescripcion().contains("Granel")){
                costoGranel+=usuMatEm.getUsuMatEmbCostoUni();
            } 
        }
        
        for(IngresosEstructura ingEs: lstIngEst){
            if(ingEs.getPreprod().getPreProdDescripcion().contains("KL")){
                ingEs.setCosto(costoKl);
            }else if(ingEs.getPreprod().getPreProdDescripcion().contains("500 GR")){
                ingEs.setCosto(costo500gr);
            }else if(ingEs.getPreprod().getPreProdDescripcion().contains("150 GR")){
                ingEs.setCosto(costo150gr);
            }else if(ingEs.getPreprod().getPreProdDescripcion().contains("110 GR")){
                ingEs.setCosto(costo110gr);
            }else if(ingEs.getPreprod().getPreProdDescripcion().contains("Granel")){
                ingEs.setCosto(costoGranel);
            }
        }
        
        totalME=0F;
        for(IngresosEstructura inEs: lstIngEst){
            if(inEs.getCosto()!=null && inEs.getCant()!=null)
                totalME+= inEs.getCosto() * inEs.getCant();
        }
        
    }
    
    public float getSumaCantMP(){
        float cantMP=0F;
        if(lstProd != null && !lstProd.isEmpty()){
            for(Producto product : getLstProd()){
                if(product.getCantMP() != null)
                    cantMP+= product.getCantMP();
            }
        }
        return cantMP;
        
    }
    
    public float getSumaCostMP(){
        float costMP=0F;
        if(lstProd != null && !lstProd.isEmpty()){
            for(Producto product: getLstProd()){
                if(product.getCostoMP()!= null)
                    costMP+= product.getCostoMP();
            }
        }
        return costMP;
    }

    public float getSumaCostCIF(){
         float costCIF=0F;
        if(lstUsuCif != null && !lstUsuCif.isEmpty()){
            for(UsuarioPorCif usucif: getLstUsuCif()){
                if(usucif.getUsuCifCosto() != null)
                    costCIF+= usucif.getUsuCifCosto();
            }
        }
        return costCIF;
    }
    
    public float getSumaValorHora(){
        float valhora=0F;
        if(lstModEst != null && !lstModEst.isEmpty()){
            for(MODEstructura modest : getLstModEst()){
                if(modest.getValorHora() != null)
                    valhora+= modest.getValorHora();
            }
        }
        return valhora;
    }
    
    public float getSumaCantHrsTrab(){
        float hrsTra=0F;
        if(lstModEst != null && !lstModEst.isEmpty()){
            for(MODEstructura modest : getLstModEst()){
                if(modest.getCantHorasTrabajadas() != null)
                    hrsTra+= modest.getCantHorasTrabajadas();
            }
        }
        return hrsTra;
    }
    
    public float getSumaValHrsExt(){
        float valHrsExt=0F;
        if(lstModEst != null && !lstModEst.isEmpty()){
            for(MODEstructura modest : getLstModEst()){
                if(modest.getValorHorasExtra() != null)
                    valHrsExt+= modest.getValorHorasExtra();
            }
        }
        return valHrsExt;
    }
    
    public float getSumaPrecioIngr(){
        float preIng=0F;
        if(lstIngEst != null && !lstIngEst.isEmpty()){
            for(IngresosEstructura ingrEst : getLstIngEst()){
                if(ingrEst.getPrecio() != null)
                    preIng+= ingrEst.getPrecio();
            }
        }
        return preIng;
    }
    
    public float getSumaCantIngr(){
        float preIng=0F;
        if(lstIngEst != null && !lstIngEst.isEmpty()){
            for(IngresosEstructura ingrEst : getLstIngEst()){
                if(ingrEst.getCant() != null)
                    preIng+= ingrEst.getCant();
            }
        }
        return preIng;
    }
    
    public float getSumaCostME(){
        float preIng=0F;
        if(lstIngEst != null && !lstIngEst.isEmpty()){
            for(IngresosEstructura ingrEst : getLstIngEst()){
                if(ingrEst.getCosto() != null)
                    preIng+= ingrEst.getCosto();
            }
        }
        return preIng;
    }
    
    public List<UsuarioPorCif> getLstUsuCif() {
        return lstUsuCif;
    }

    public void setLstUsuCif(List<UsuarioPorCif> lstUsuCif) {
        this.lstUsuCif = lstUsuCif;
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

    public Float getSumaCif() {
        return sumaCif;
    }

    public void setSumaCif(Float sumaCif) {
        this.sumaCif = sumaCif;
    }

    public Boolean getGenerado() {
        return generado;
    }

    public void setGenerado(Boolean generado) {
        this.generado = generado;
    }

    public List<ProduccionDiaria> getLstProduDia() {
        return lstProduDia;
    }

    public void setLstProduDia(List<ProduccionDiaria> lstProduDia) {
        this.lstProduDia = lstProduDia;
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

    public Float getTotalMP() {
        return totalMP;
    }

    public void setTotalMP(Float totalMP) {
        this.totalMP = totalMP;
    }

    public List<Usuario> getLstUsu() {
        return lstUsu;
    }

    public void setLstUsu(List<Usuario> lstUsu) {
        this.lstUsu = lstUsu;
    }

    public List<MODEstructura> getLstModEst() {
        return lstModEst;
    }

    public void setLstModEst(List<MODEstructura> lstModEst) {
        this.lstModEst = lstModEst;
    }

    public Float getTotalMOD() {
        return totalMOD;
    }

    public void setTotalMOD(Float totalMOD) {
        this.totalMOD = totalMOD;
    }

    public List<PresentacionProducto> getLstPreProd() {
        return lstPreProd;
    }

    public void setLstPreProd(List<PresentacionProducto> lstPreProd) {
        this.lstPreProd = lstPreProd;
    }

    public List<IngresosEstructura> getLstIngEst() {
        return lstIngEst;
    }

    public void setLstIngEst(List<IngresosEstructura> lstIngEst) {
        this.lstIngEst = lstIngEst;
    }

    public Float getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(Float totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public Float getTotalME() {
        return totalME;
    }

    public void setTotalME(Float totalME) {
        this.totalME = totalME;
    }

    public Float getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(Float totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

  

    
    
}

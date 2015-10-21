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
import Entidades.Producto;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import Entidades.UsuarioPorCif;
import Entidades.UsuarioPorMaterialEmbalaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
    private Boolean generado=false;
    
    public void BalanceController(){
        generado=false;
        fechaIni = null;
    }
    
    public void generarReporte(){
        calcularCIF();
        calcularProduccionDiaria();
        calcularMP();
        calcularMOD();
        
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
            List<TiemposProduccion> lstTiempos = ejbTiemFacade.lstTiemUsuFecha(mood.getEmpleado(), fechaIni, fechaFin);
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
            if(preprod.getPreProdDescripcion().equals("kL")){
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

    
    
}

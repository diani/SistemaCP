/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.MaterialEmbalaje;
import Entidades.ProduccionDiaria;
import Entidades.Producto;
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
    private Date fechaIni;
    private Date fechaFin = new Date();
    private List<UsuarioPorCif> lstUsuCif = null;
    private List<ProduccionDiaria> lstProduDia = null;
    private List<Producto> lstProd = null;
    private List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb = null;
    private Float sumaCif;
    private Float totalMP;
    private Boolean generado=false;
    
    public void BalanceController(){
        generado=false;
        fechaIni = null;
    }
    
    public void generarReporte(){
        calcularCIF();
        calcularProduccionDiari();
        calcularMP();
        
        
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
    
    public void calcularProduccionDiari(){
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
    
    public float sumaCantMP(){
        float cantMP=0F;
        for(Producto product: lstProd){
            if(product.getCantMP() != null)
                cantMP+= product.getCantMP();
        }
        return cantMP;
    }
    
    public float sumaCostMP(){
        float costMP=0F;
        for(Producto product: lstProd){
            if(product.getCostoMP()!= null)
                costMP+= product.getCostoMP();
        }
        return costMP;
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

    
    
}

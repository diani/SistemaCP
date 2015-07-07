/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PresentacionProducto;
import Entidades.ProduccionDiaria;
import Entidades.ProduccionPorPresentacion;
import Entidades.Producto;
import Entidades.UnidadesMp;
import Entidades.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author diani
 */
@ManagedBean(name = "produccionDiariaController")
@SessionScoped
public class ProduccionDiariaController implements Serializable {
    
    @EJB
    private Controladores.ProduccionDiariaFacade ejbProduccionDiaFacade;
    @EJB
    private Controladores.UsuarioFacade ejbUsuarioFacade;
    @EJB
    private Controladores.ProductoFacade ejbProductoFacade;
    @EJB
    private Controladores.UnidadesMpFacade ejbUniMPFacade;
    @EJB
    private Controladores.PresentacionProductoFacade ejbPreProdFacade;
    @EJB
    private Controladores.ProduccionPorPresentacionFacade ejbProduccionPreFacade;
    private List<Usuario> usuarios = null;
    private Usuario selectedUsu;
    private List<Producto> productos = null;
    private Producto selectedProd;
    private List<UnidadesMp> unidadesMp = null;
    private UnidadesMp selectedUniMP;
    private List<PresentacionProducto> presProd = null;
    private PresentacionProducto selectedPreProd;
    private List<ProduccionPorPresentacion> produccionPre = null;
    private ProduccionPorPresentacion selectedProduccionPre;
    private List<ProduccionDiaria> produccionDia = new ArrayList<ProduccionDiaria>();
    private ProduccionDiaria selectedProduccionDia;
    private List<Producto> prodizq = new ArrayList<Producto>();
    private List<Producto> prodder = new ArrayList<Producto>();
    private DualListModel<Producto> productosdual = new DualListModel<Producto>(prodizq,prodder);
    

     public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda Cambiada", "Antigua: " + (oldValue != null ? oldValue : "-") + ", Nueva:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
     
     public void abrirProductos(){
        prodizq = ejbProductoFacade.lstProductosHabilitados(true);
        prodder = new ArrayList<Producto>();
        for(ProduccionDiaria var: produccionDia){
            prodder.add(var.getProdCodigo());
        }
        for(Producto aux: prodder){
            if(prodizq.contains(aux)){
                prodizq.remove(aux);
            }
        }
        productosdual = new DualListModel<Producto>(prodizq, prodder);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('UnidadMPDialog').show();");
    }
     
     public void enviarProductos(Usuario usu){
        if(produccionDia != null && !produccionDia.isEmpty()){
            for(Producto prod: productosdual.getTarget()){
                Boolean contiene = false;
                for(ProduccionDiaria prodDia :produccionDia){
                    if(prodDia.getProdCodigo().equals(prod)){
                       contiene = true;
                    }
                }
                if(!contiene){
                    ProduccionDiaria prodDia = new ProduccionDiaria();
                    prodDia.setProdCodigo(prod);
                    prodDia.setProdDiaFecha(new Date());
                    prodDia.setUsuId(usu);
                    produccionDia.add(prodDia);
                }
                    
            }
        }else{
             for(Producto prod: productosdual.getTarget()){
                ProduccionDiaria prodDia = new ProduccionDiaria();
                    prodDia.setProdCodigo(prod);
                    prodDia.setProdDiaFecha(new Date());
                    prodDia.setUsuId(usu);
                    produccionDia.add(prodDia);
            } 
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('UnidadMPDialog').hide();");   
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getSelectedUsu() {
        return selectedUsu;
    }

    public void setSelectedUsu(Usuario selectedUsu) {
        this.selectedUsu = selectedUsu;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Producto getSelectedProd() {
        return selectedProd;
    }

    public void setSelectedProd(Producto selectedProd) {
        this.selectedProd = selectedProd;
    }

    public List<UnidadesMp> getUnidadesMp() {
        unidadesMp = ejbUniMPFacade.uniMPhabilitadas(true);
        return unidadesMp;
    }

    public void setUnidadesMp(List<UnidadesMp> unidadesMp) {
        this.unidadesMp = unidadesMp;
    }

    public UnidadesMp getSelectedUniMP() {
        return selectedUniMP;
    }

    public void setSelectedUniMP(UnidadesMp selectedUniMP) {
        this.selectedUniMP = selectedUniMP;
    }

    public List<PresentacionProducto> getPresProd() {
        return presProd;
    }

    public void setPresProd(List<PresentacionProducto> presProd) {
        this.presProd = presProd;
    }

    public PresentacionProducto getSelectedPreProd() {
        return selectedPreProd;
    }

    public void setSelectedPreProd(PresentacionProducto selectedPreProd) {
        this.selectedPreProd = selectedPreProd;
    }

    public List<ProduccionPorPresentacion> getProduccionPre() {
        return produccionPre;
    }

    public void setProduccionPre(List<ProduccionPorPresentacion> produccionPre) {
        this.produccionPre = produccionPre;
    }

    public ProduccionPorPresentacion getSelectedProduccionPre() {
        return selectedProduccionPre;
    }

    public void setSelectedProduccionPre(ProduccionPorPresentacion selectedProduccionPre) {
        this.selectedProduccionPre = selectedProduccionPre;
    }

    public List<ProduccionDiaria> getProduccionDia() {
        return produccionDia;
    }

    public void setProduccionDia(List<ProduccionDiaria> produccionDia) {
        this.produccionDia = produccionDia;
    }

    public ProduccionDiaria getSelectedProduccionDia() {
        return selectedProduccionDia;
    }

    public void setSelectedProduccionDia(ProduccionDiaria selectedProduccionDia) {
        this.selectedProduccionDia = selectedProduccionDia;
    }

    public List<Producto> getProdizq() {
        return prodizq;
    }

    public void setProdizq(List<Producto> prodizq) {
        this.prodizq = prodizq;
    }

    public List<Producto> getProdder() {
        return prodder;
    }

    public void setProdder(List<Producto> prodder) {
        this.prodder = prodder;
    }

    public DualListModel<Producto> getProductosdual() {
        return productosdual;
    }

    public void setProductosdual(DualListModel<Producto> productosdual) {
        this.productosdual = productosdual;
    }
    
    
}

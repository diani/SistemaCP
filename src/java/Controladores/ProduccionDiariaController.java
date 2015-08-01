/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.util.JsfUtil;
import Entidades.ActividadPorTarea;
import Entidades.PresentacionProducto;
import Entidades.ProcesoPorActividad;
import Entidades.ProduccionDiaria;
import Entidades.ProduccionDiariaEstructura;
import Entidades.ProduccionPorPresentacion;
import Entidades.ProduccionPorPresentacionPK;
import Entidades.Producto;
import Entidades.UnidadesMp;
import Entidades.Usuario;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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
    private List<ProduccionDiariaEstructura> lstproddia = new ArrayList<ProduccionDiariaEstructura>();
    private ProduccionDiariaEstructura selectedProdDiaEst;
    private Boolean habilitarGuardar = true;

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
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda Cambiada", "Antigua: " + (oldValue != null ? oldValue : "-") + ", Nueva:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public String nuevaProduccionDiaria(){
        produccionDia = new ArrayList<ProduccionDiaria>();
        return "/produccionDiaria/produccionDiaria.xhtml";
    }
    
    public String verProduccionDiaria(){
        produccionDia = new ArrayList<ProduccionDiaria>();
        produccionDia = selectedProdDiaEst.getLstProdDia();
        for(ProduccionDiaria tot :produccionDia){
            tot.setProduccionPorPresentacionList(ejbProduccionPreFacade.buscarProPorPrePorParamProdu(tot));
            tot.setTotalProdT(0F);
            for(ProduccionPorPresentacion cantTot :tot.getProduccionPorPresentacionList()){
                tot.setTotalProdT((tot.getTotalProdT() != null ? tot.getTotalProdT():0F)+(cantTot.getProdPorPresCantPt()!= null ? cantTot.getProdPorPresCantPt() : 0F)*cantTot.getPresentacionProducto().getPreProdEquivalenciaPt());
            }
        }
        return "/produccionDiaria/produccionDiaria.xhtml";
        
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
    
    public void abrirPresentacionProducto(){
        presProd = new ArrayList<PresentacionProducto>();
        presProd = ejbPreProdFacade.preseProdHabilitadas(true);
        if(selectedProduccionDia.getProduccionPorPresentacionList()== null || selectedProduccionDia.getProduccionPorPresentacionList().isEmpty()){
            selectedProduccionDia.setProduccionPorPresentacionList(new ArrayList<ProduccionPorPresentacion>());
            for(PresentacionProducto aux :presProd){
                ProduccionPorPresentacion var = new ProduccionPorPresentacion();
                var.setProduccionPorPresentacionPK(new ProduccionPorPresentacionPK());
                var.setPresentacionProducto(aux);
                var.setProduccionDiaria(selectedProduccionDia);
                var.getProduccionPorPresentacionPK().setProdDiaCodigo(aux.getPreProdCodigo());
                selectedProduccionDia.getProduccionPorPresentacionList().add(var);
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('PulpaDialog').show();");
    }
    
    public void eliminarPresProd(){
        if(selectedProduccionPre.getProduccionPorPresentacionPK().getPreProdCodigo()==0){
            selectedProduccionDia.getProduccionPorPresentacionList().remove(selectedProduccionPre);
        }else{
            selectedProduccionDia.getProduccionPorPresentacionList().remove(selectedProduccionPre);
            ejbProduccionPreFacade.remove(selectedProduccionPre);
        }
    }
    
    public void agregarPresProd(){
        for(PresentacionProducto aux :presProd){
            Boolean contiene = false;
            for(ProduccionPorPresentacion proPre:selectedProduccionDia.getProduccionPorPresentacionList()){
                if(proPre.getPresentacionProducto().getPreProdDescripcion().equals(aux.getPreProdDescripcion())){
                    contiene = true;
                }
            }
            if(!contiene){
                ProduccionPorPresentacion var = new ProduccionPorPresentacion();
                var.setProduccionPorPresentacionPK(new ProduccionPorPresentacionPK());
                var.setPresentacionProducto(aux);
                var.setProduccionDiaria(selectedProduccionDia);
                var.getProduccionPorPresentacionPK().setProdDiaCodigo(aux.getPreProdCodigo());
                selectedProduccionDia.getProduccionPorPresentacionList().add(var);    
                break;
            }
        }
        JsfUtil.addErrorMessage("Ya no existen presentaciones disponibles");
    }
    
    public void sumarTotales(){
        Float total =0F;
        for(ProduccionPorPresentacion proPre:selectedProduccionDia.getProduccionPorPresentacionList()){
            total += (proPre.getProdPorPresCantPt()!= null ? proPre.getProdPorPresCantPt() : 0F)*proPre.getPresentacionProducto().getPreProdEquivalenciaPt();
            
        }
        selectedProduccionDia.setTotalProdT(total);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('PulpaDialog').hide();");
    }
    
    public String guardarProdDiaria(Usuario usu){
        try{
            for(ProduccionDiaria prd: produccionDia ){
                prd.setUsuId(usu);
                produccionPre = new ArrayList<ProduccionPorPresentacion>();
                produccionPre = prd.getProduccionPorPresentacionList();
                prd.setProduccionPorPresentacionList(null);
                if(prd.getProdDiaCodigo() == null){
                    ejbProduccionDiaFacade.persist(prd);
                }else
                    ejbProduccionDiaFacade.merge(prd);
                if(produccionPre != null && !produccionPre.isEmpty()){
                    for(ProduccionPorPresentacion prpre :produccionPre){
                        if(prpre.getProduccionPorPresentacionPK().getProdDiaCodigo()==0 || prpre.getProduccionPorPresentacionPK().getPreProdCodigo()==0){
                            prpre.setProduccionDiaria(prd);
                            prpre.getProduccionPorPresentacionPK().setProdDiaCodigo(prd.getProdDiaCodigo());
                            prpre.getProduccionPorPresentacionPK().setPreProdCodigo(prpre.getPresentacionProducto().getPreProdCodigo());
                            ejbProduccionPreFacade.persist(prpre);
                        }else{
                            ejbProduccionPreFacade.merge(prpre);
                        }
                    }
                }
            }
            JsfUtil.addSuccessMessage("Guardado Satisfactoriamente");
            
        }
        catch(Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return "/produccionDiaria/principalProduccionDiaria?faces-redirect=true";
    }

    public ProduccionDiariaEstructura getSelectedProdDiaEst() {
        return selectedProdDiaEst;
    }

    public void setSelectedProdDiaEst(ProduccionDiariaEstructura selectedProdDiaEst) {
        this.selectedProdDiaEst = selectedProdDiaEst;
    }

    public List<ProduccionDiariaEstructura> getLstproddia() {
        lstproddia=ejbProduccionDiaFacade.buscarFechaUsu();
        for(ProduccionDiariaEstructura aux:lstproddia){
            aux.setLstProdDia(ejbProduccionDiaFacade.buscarPorFechaUsuario(aux.getProdDiaEstFecha(), aux.getCodigoUsuario()));
            aux.setCantidadFruta("" +aux.getLstProdDia().size());
        }
        for(ProduccionDiariaEstructura temp:lstproddia){
            if(JsfUtil.convertirFechaAString(temp.getProdDiaEstFecha()).equals(JsfUtil.convertirFechaAString(new Date()))){
                habilitarGuardar = false;
                break;
            }else{
                habilitarGuardar = true;
            }
        }
        
        return lstproddia;
    }

    public void setLstproddia(List<ProduccionDiariaEstructura> lstproddia) {
        this.lstproddia = lstproddia;
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

    public Boolean getHabilitarGuardar() {
        return habilitarGuardar;
    }

    public void setHabilitarGuardar(Boolean habilitarGuardar) {
        this.habilitarGuardar = habilitarGuardar;
    }
    
    
}
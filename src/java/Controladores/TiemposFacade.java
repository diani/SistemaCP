/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
import Entidades.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author diani
 */
@Stateless
public class TiemposFacade extends AbstractFacade<TiemposProduccion> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiemposFacade() {
        super(TiemposProduccion.class);
    }
    
    public List<TiemposProduccion> tiempoPorPlanificacion(PlanificacionProcesos seleccionapp){
        try {
                return findByParameters("from TiemposProduccion tp where tp.plaProcCodigo.plaProcCodigo = ?1", seleccionapp.getPlaProcCodigo());
        } catch (Exception e) {
                return null;
        }
    }    
    
    public TiemposProduccion ultimoTiempoPorUsuario(PlanificacionProcesos planiProc,Usuario usuario){
        try {
                return findByParameters("from TiemposProduccion tp where tp.plaProcCodigo.plaProcCodigo = ?1 and tp.usuId.usuId = ?2 order by tp.tieProdCodigo desc", planiProc.getPlaProcCodigo(), usuario.getUsuId()).get(0);
        } catch (Exception e) {
                return null;
        }
    }  
    
    public TiemposProduccion ultimoTiempoPorUsuarioYdia(Usuario usuario){
        try {
                return findByParameters("from TiemposProduccion tp where tp.usuId.usuId = ?1 order by tp.tieProdCodigo desc", usuario.getUsuId()).get(0);
        } catch (Exception e) {
                return null;
        }
    }
    
    public TiemposProduccion penultimoTiempoPorUsuarioYdia(Usuario usuario){
        try {
                return findByParameters("from TiemposProduccion tp where tp.usuId.usuId = ?1 order by tp.tieProdCodigo desc", usuario.getUsuId()).get(1);
        } catch (Exception e) {
                return null;
        }
    }
    
    public List<TiemposProduccion> lstTiemUsuFecha(Usuario usuario, Date fechaini, Date fechafin){
        try {
                return findByParameters("from TiemposProduccion tp where tp.usuId.usuId = ?1 and ((tp.tieProdHoraIni between ?2 and ?3) or (tp.tieProdHoraFin between ?2 and ?3))  order by tp.tieProdCodigo asc", usuario.getUsuId(), fechaini, fechafin);
        } catch (Exception e) {
                return null;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionProcesos;
import Entidades.TiemposProduccion;
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
    
}

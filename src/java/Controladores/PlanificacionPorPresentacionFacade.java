/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionPorPresentacion;
import Entidades.PlanificacionProcesos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class PlanificacionPorPresentacionFacade extends AbstractFacade<PlanificacionPorPresentacion> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanificacionPorPresentacionFacade() {
        super(PlanificacionPorPresentacion.class);
    }
    
    public List<PlanificacionPorPresentacion> buscarPlaPorPrePorParamPlani(PlanificacionProcesos plaproc){
        try {
                return findByParameters("from PlanificacionPorPresentacion ppre where ppre.plaProcCodigo.plaProcCodigo = ?1", plaproc.getPlaProcCodigo());
        } catch (Exception e) {
                return null;
        }
    }
    
}
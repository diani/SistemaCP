/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PlanificacionProcesos;
import Entidades.Proceso;
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
public class PlanificacionFacade extends AbstractFacade<PlanificacionProcesos> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanificacionFacade() {
        super(PlanificacionProcesos.class);
    }
    
    public List<PlanificacionProcesos> buscarTodosOrdenadosFecha() {
        try {
                return find("from PlanificacionProcesos pp order by pp.plaProcFechaIni desc, pp.plaProcCodigo desc");
        } catch (Exception e) {
                return null;
        }
    }
    
    public List<PlanificacionProcesos> buscarFechaIni() {
        try {
                return findByParameters("from PlanificacionProcesos pp where pp.plaProcHabilitado=?1 and ?2 between pp.plaProcFechaIni and pp.plaProcFechaFin  order by pp.plaProcFechaIni desc, pp.plaProcFechaFin desc", true, new Date());
        } catch (Exception e) {
                return null;
        }
    }
}
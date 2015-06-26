/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Actividad;
import Entidades.Proceso;
import Entidades.ProcesoPorActividad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class ProcesoPorActividadFacade extends AbstractFacade<ProcesoPorActividad> {
     @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcesoPorActividadFacade() {
        super(ProcesoPorActividad.class);
    }
    
    public ProcesoPorActividad buscarPorProcesoyActividad (Proceso proceso, Actividad actividad){
        try {
                return findByParameters("from ProcesoPorActividad p where p.procCodigo.procCodigo = ?1 and p.actCodigo.actCodigo = ?2", proceso.getProcCodigo(), actividad.getActCodigo()).get(0);
        } catch (Exception e) {
                return null;
        }
    }
    
    public List<ProcesoPorActividad> buscarListaDeProcesosActivdadPorProceso (Proceso proceso){
        try {
                return findByParameters("from ProcesoPorActividad p where p.procCodigo.procCodigo = ?1 order by p.procActOrden asc", proceso.getProcCodigo());
        } catch (Exception e) {
                return null;
        }
    }
}

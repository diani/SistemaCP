/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Proceso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class ProcesoFacade extends AbstractFacade<Proceso> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcesoFacade() {
        super(Proceso.class);
    }
    
    public List<Proceso> buscarTodosPorHabiltiado(Boolean habilitado) {
        try {
                return findByParameters("from Proceso p where p.procHabilitadoInterno = ?1", habilitado);
        } catch (Exception e) {
                return null;
        }
    }

}

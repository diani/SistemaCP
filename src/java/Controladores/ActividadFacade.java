/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Actividad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class ActividadFacade extends AbstractFacade<Actividad> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadFacade() {
        super(Actividad.class);
    }
    
    public List<Actividad> actividadeshabilitadas(Boolean habilitado){
        try {
                return findByParameters("from Actividad ac where ac.actHabilitado = ?1", habilitado);
        } catch (Exception e) {
                return null;
        }
    }
}

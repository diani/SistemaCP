/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.ActividadPorTarea;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author diani
 */
@Stateless
public class ActividadPorTareaFacade extends AbstractFacade<ActividadPorTarea>{
     @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadPorTareaFacade() {
        super(ActividadPorTarea.class);
    }
}

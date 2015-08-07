/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Cif;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class CifFacade extends AbstractFacade<Cif> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CifFacade() {
        super(Cif.class);
    }
    public List<Cif> buscarTodosPorHabiltiado(Boolean habilitado) {
        try {
                return findByParameters("from Cif c where c.cifHabilitado = ?1", habilitado);
        } catch (Exception e) {
                return null;
        }
    }
}

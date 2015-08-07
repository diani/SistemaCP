/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.UsuarioPorCif;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class UsuarioPorCifFacade extends AbstractFacade<UsuarioPorCif> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioPorCifFacade() {
        super(UsuarioPorCif.class);
    }
    
    public List<UsuarioPorCif> lstFecha()
    {
        try {
                return find("from UsuarioPorCif usuCif order by usuCif.usuCifFecha");
        } catch (Exception e) {
                return null;
        }
    }
    
}

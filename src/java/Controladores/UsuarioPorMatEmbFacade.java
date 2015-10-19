/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.UsuarioPorCif;
import Entidades.UsuarioPorMaterialEmbalaje;
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
public class UsuarioPorMatEmbFacade extends AbstractFacade<UsuarioPorMaterialEmbalaje> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioPorMatEmbFacade() {
        super(UsuarioPorMaterialEmbalaje.class);
    }
    
    public List<UsuarioPorMaterialEmbalaje> lstFecha()
    {
        try {
                return find("from UsuarioPorMaterialEmbalaje usuMaEm order by usuMaEm.usuMatEmbFecha");
        } catch (Exception e) {
                return null;
        }
    }
    public List<UsuarioPorMaterialEmbalaje> lstUsuMatEmb(Date fechaIn, Date fechaFi){
        try {
                return findByParameters("from UsuarioPorMaterialEmbalaje me where me.usuMatEmbFecha between ?1 and ?2", fechaIn, fechaFi);
        } catch (Exception e) {
                return null;
        }
    }
}


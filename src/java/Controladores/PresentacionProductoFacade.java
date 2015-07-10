/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.PresentacionProducto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class PresentacionProductoFacade extends AbstractFacade<PresentacionProducto> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PresentacionProductoFacade() {
        super(PresentacionProducto.class);
    }
    
    public List<PresentacionProducto> preseProdHabilitadas(Boolean habilitado){
        try {
                return findByParameters("from PresentacionProducto prepro where prepro.preProdHabilitado = ?1", habilitado);
        } catch (Exception e) {
                return null;
        }
    }
}

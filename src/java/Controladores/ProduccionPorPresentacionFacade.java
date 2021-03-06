/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.ProduccionDiaria;
import Entidades.ProduccionPorPresentacion;
import Entidades.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class ProduccionPorPresentacionFacade extends AbstractFacade<ProduccionPorPresentacion> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProduccionPorPresentacionFacade() {
        super(ProduccionPorPresentacion.class);
    }
    
    public List<ProduccionPorPresentacion> buscarProPorPrePorParamProdu(ProduccionDiaria produDia){
        try {
                return findByParameters("from ProduccionPorPresentacion produpre where produpre.produccionDiaria.prodDiaCodigo = ?1", produDia.getProdDiaCodigo());
        } catch (Exception e) {
                return null;
        }
    }
    
}

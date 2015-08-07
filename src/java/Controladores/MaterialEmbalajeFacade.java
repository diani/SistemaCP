/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Cif;
import Entidades.MaterialEmbalaje;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class MaterialEmbalajeFacade extends AbstractFacade<MaterialEmbalaje> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MaterialEmbalajeFacade() {
        super(MaterialEmbalaje.class);
    }
    
    public List<MaterialEmbalaje> buscarTodosPorHabiltiado(Boolean habilitado) {
        try {
                return findByParameters("from MaterialEmbalaje me where me.matEmbHabilitado = ?1", habilitado);
        } catch (Exception e) {
                return null;
        }
    }
    
}

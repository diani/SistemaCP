/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author diani
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario UsuClave(String usu, String clave, Boolean habilitado){
        try {
              return findByParameters("from Usuario u where u.usuUsuario= ?1 and u.usuClave= ?2 and u.usuHabilitado= ?3",usu ,clave,habilitado).get(0);
        } catch (Exception e) {
                return null;
        }
    }
    
}

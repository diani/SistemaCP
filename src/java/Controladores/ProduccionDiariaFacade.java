/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.ProduccionDiaria;
import Entidades.ProduccionDiariaEstructura;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class ProduccionDiariaFacade extends AbstractFacade<ProduccionDiaria> {
    @PersistenceContext(unitName = "SistemaCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProduccionDiariaFacade() {
        super(ProduccionDiaria.class);
    }
    
    public List<ProduccionDiariaEstructura> buscarFechaUsu() {
        try {
                List<ProduccionDiariaEstructura> lstProduccionDiariaEst = new ArrayList<ProduccionDiariaEstructura>();
                String query = " ";
		
		query = "SELECT  p.prod_dia_fecha , u.usu_nombre, u.USU_APELLIDO, u.USU_ID FROM sistemacp.produccion_diaria p natural join sistemacp.usuario u group by p.prod_dia_fecha , u.usu_nombre;";	

		System.out.println(query);
		
		List<Object[]> result =  em.createNativeQuery(query).getResultList();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
                for (Object[] aux : result){
                    ProduccionDiariaEstructura var = new ProduccionDiariaEstructura(formatter.parse(aux[0].toString()), aux[1].toString() + " " +aux[2].toString() , Integer.valueOf(aux[3].toString()));
                    lstProduccionDiariaEst.add(var);
                }
                return lstProduccionDiariaEst;
                
        } catch (Exception e) {
                return null;
        }
    }
    
    public List<ProduccionDiaria> buscarPorFechaUsuario(Date fecha, Integer codigoUsuario){
        try {
              return findByParameters("from ProduccionDiaria u where u.prodDiaFecha = ?1 and u.usuId.usuId = ?2",fecha,codigoUsuario);
        } catch (Exception e) {
                return null;
        }
    }
}

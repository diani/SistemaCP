/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author diani
 */
public abstract class AbstractFacade<T> {
    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
    
    public T persist(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }
    
    public T merge(T entity) {
        getEntityManager().merge(entity);
        return entity;
    }
    

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<T> findByParameters(String query, Object... args)
			throws Exception {
        try {
            final Query q = this.entityManager.createQuery(query);

            if (args != null) {
                    int position = 1;
                    for (Object obj : args) {
                            q.setParameter(position++, obj);
                    }
            }
            q.setMaxResults(100);
            @SuppressWarnings("unchecked")
            final List<T> result = q.getResultList();
            return result;
        } catch (final Exception e) {
                throw new Exception(e);
        }
    }
    
    public List<T> find(String query) throws Exception {
		try {
			final Query q = this.entityManager.createQuery(query);
			q.setMaxResults(100);
			@SuppressWarnings("unchecked")
			final List<T> result = q.getResultList();
			return result;
		} catch (final Exception e) {
			throw new Exception(e);
		}
	}
}

package org.springframework.data.cassandra.repository.support;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository base implementation for Cassandra.
 * 
 * @author Brian O'Neill
 */
public class CassandraJpaRepository<T, ID extends Serializable> implements PagingAndSortingRepository<T, ID> {

    EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private Class<T> entityType = null;

    @SuppressWarnings("unchecked")
    public CassandraJpaRepository() {
        Type returnType = getClass().getGenericSuperclass();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) returnType;
            this.entityType = (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

    // @PersistenceContext
    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#findOne(java.io.
     * Serializable)
     */
    public T findOne(ID id) {
        return entityManager.find(this.entityType, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#exists(java.io.
     * Serializable)
     */
    public boolean exists(ID id) {
        T entity = entityManager.find(this.entityType, id);
        return (entity != null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#count()
     */
    public long count() {
        // TODO: Use count() syntax here, doesn't appear to be working... 
        //       need to check on status of count() in CQL.  
        //       Still costly across the entire cluster?
        //EntityMetadata metadata = KunderaMetadataManager.getEntityMetadata(this.entityType);
        String jpqQuery = "select u from " + this.entityType.getSimpleName() + " u";
        Query query = entityManager.createQuery(jpqQuery);
        List<Long> result = query.getResultList();
        return result.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#delete(java.io.
     * Serializable)
     */
    public void delete(ID id) {
        T entity = entityManager.find(this.entityType, id);
        this.delete(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.CrudRepository#delete(java.lang.Object
     * )
     */
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable
     * )
     */
    public void delete(Iterable<? extends T> entities) {
        // TODO: Come back and potentially use ranges here.
        for (T entity: entities){
            this.delete(entity);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#deleteAll()
     */
    public void deleteAll() {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    public List<T> findAll() {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.CrudRepository#findAll(java.lang.
     * Iterable)
     */
    public Iterable<T> findAll(Iterable<ID> ids) {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.PagingAndSortingRepository#findAll
     * (org.springframework.data.domain.Pageable)
     */
    public Page<T> findAll(final Pageable pageable) {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.PagingAndSortingRepository#findAll
     * (org.springframework.data.domain.Sort)
     */
    public List<T> findAll(final Sort sort) {
        throw new NotImplementedException();
    }

	public <S extends T> S save(S entity) {
        this.entityManager.persist(entity);
        return entity;
	}

	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.bizlers.geoq.discovery.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.Collection;

public class BaseMongoDao<PK, T extends Serializable> implements IGenericDao<T, PK> {

	@Autowired
	@Qualifier("datastore")
	private Datastore ds;

	private Class<T> entityClass;
	
	public BaseMongoDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected Datastore getDatastore() {
		return ds;
	}
	
	@Override
	public T find(PK id) {
		return ds.get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK create(T entity) {
		Key<T> key = ds.save(entity);
		return (PK) key.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK update(T entity) {
		Key<T> key = ds.save(entity);
		return (PK) key.getId();
	}

	public Collection<T> findAll() {
		Query query = ds.find(entityClass);
		return query.asList();
	}

	@Override
	public void delete(T entity) {
		ds.delete(entity);
	}

	@Override
	public void deleteById(PK entityId) {
		ds.delete(entityClass, entityId);
	}
}

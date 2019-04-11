package com.bizlers.geoq.discovery.dao;

import java.io.Serializable;

public interface IGenericDao<T extends Serializable, PK> {

	T find(final PK id);

	PK create(final T entity);

	PK update(final T entity);

	void delete(final T entity);

	void deleteById(final PK entityId);
}
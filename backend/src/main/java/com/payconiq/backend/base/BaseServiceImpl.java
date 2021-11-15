package com.payconiq.backend.base;

import com.payconiq.backend.exception.EntityNotFoundException;
import com.payconiq.backend.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Number> implements BaseService<T, ID> {

    @Autowired
    protected EntityMapper<T> entityMapper;

    @Override
    public T findById(ID id) {
        return getRepository().findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "entity with id " + id + " does not exist!"));
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T create(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public T update(ID id, T dataEntity) throws EntityNotFoundException {
        T entityToBeUpdated = findById(id);
        entityMapper.mapIgnoreNullValues(dataEntity, entityToBeUpdated);
        return getRepository().save(entityToBeUpdated);
    }

    @Override
    public boolean delete(ID id) throws EntityNotFoundException {
        T entityToBeDeleted = findById(id);
        getRepository().delete(entityToBeDeleted);
        return Boolean.TRUE;
    }
}

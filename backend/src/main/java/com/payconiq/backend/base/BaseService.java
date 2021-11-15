package com.payconiq.backend.base;

import com.payconiq.backend.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseService<T extends BaseEntity, L extends Number> {

    JpaRepository<T, L> getRepository();

    T findById(L id) throws EntityNotFoundException;

    List<T> findAll();

    T create(T entity);

    T update(L id, T entity) throws EntityNotFoundException;

    boolean delete(L id) throws EntityNotFoundException;
}

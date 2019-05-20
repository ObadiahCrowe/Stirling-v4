package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.util.UpdateLevel;

import java.util.Set;

public interface BaseService<E> {

    E update(E entity, UpdateLevel level) throws Exception;

    E delete(E entity);

    E getById(Long id);

    Set<E> getAll();

}

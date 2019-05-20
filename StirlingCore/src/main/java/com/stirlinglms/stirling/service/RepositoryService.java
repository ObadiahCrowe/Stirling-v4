package com.stirlinglms.stirling.service;

public interface RepositoryService<E, R> extends BaseService<E> {

    R getRepository();
}

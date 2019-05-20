package com.stirlinglms.stirling.entity;

import com.stirlinglms.stirling.service.RepositoryService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import javax.annotation.concurrent.Immutable;

@Immutable
public interface SaveableEntity<E, T, S extends RepositoryService<E, A>, A extends DatastoreRepository<E, Long>> extends Entity<T> {

    /*
     * E - Entity
     * T - Transport
     * S - Service
     * A - Access
     */

    default E save(E entity) {
        return this.getDao().save(entity);
    }

    default E update(E entity) throws Exception {
        return this.getService().update(entity, UpdateLevel.SYSTEM);
    }

    S getService();

    default A getDao() {
        return this.getService().getRepository();
    }
}

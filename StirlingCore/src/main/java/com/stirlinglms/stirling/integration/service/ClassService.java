package com.stirlinglms.stirling.integration.service;

import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.exception.EntityUpdateException;
import com.stirlinglms.stirling.integration.entity.ImportableClass;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

/**
 * Represents a basic platform import service.
 *
 * @param <I> Identifier type for each imported course.
 * @param <C> Class object from return.
 */
@ThreadSafe
public interface ClassService<I, C extends ImportableClass<I>> {

    Set<ImportableClass<I>> getCoursesList(User user) throws Exception;

    boolean validCredentials(User user) throws EntityUpdateException;

    C getCourse(User user, ImportableClass<I> identifier);

    Set<C> getCourses(User user);
}

package org.sportsclub.dam.sports_club_manager.service.validation;

/** Typed contract for validating one domain object. */
public interface DomainValidator<T> {
    void validate(T entity);
}

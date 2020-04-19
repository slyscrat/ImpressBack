package com.slyscrat.impress.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object id) {
        super(String.format("No entity found with such id: [%s]", id));
    }

    public EntityNotFoundException(Class<?> cls, Object id) {
        super(String.format("[%s] - cannot find entity with such id: [%s]", cls.getCanonicalName(), id));
    }
}

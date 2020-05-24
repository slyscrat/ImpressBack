package com.slyscrat.impress.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(Object id) {
        super(String.format("Entity already exists with such uid: [%s]", id));
    }

    public EntityAlreadyExistsException(Class<?> cls, Object id) {
        super(String.format("[%s] - entity already exists with such uid: [%s]", cls.getCanonicalName(), id));
    }
}

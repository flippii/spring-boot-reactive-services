package com.spring.boot.example.core.domain;

import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public abstract class AbstractDocument<PK extends Serializable> {

    public abstract PK getId();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        AbstractDocument<?> that = (AbstractDocument<?>) obj;

        return ObjectUtils.nullSafeEquals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this);
    }

    @Override
    public String toString() {
        return String.format("Document of type %s with id: %s", getClass().getName(), getId());
    }

}

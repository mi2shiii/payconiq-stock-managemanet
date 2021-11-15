package com.payconiq.backend.util;

import com.payconiq.backend.base.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

@Component
public class EntityMapper<T extends BaseEntity> {

    public void map(T source, T destination) {
        BeanUtils.copyProperties(source, destination);
    }

    public void mapIgnoreNullValues(T source, T destination) {
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors()).map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null).toArray(String[]::new);
    }
}

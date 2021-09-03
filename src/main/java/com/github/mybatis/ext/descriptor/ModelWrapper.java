package com.github.mybatis.ext.descriptor;


import com.github.mybatis.ext.enumdata.EnumValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ModelWrapper<T> {

    private final static Logger logger = LoggerFactory.getLogger(ModelWrapper.class);

    private Object model;

    private Map<String, Object> columnValueMapWithoutPK;

    private ModelDescriptor<T> descriptor;

    private Lock lock = new ReentrantLock();

    public ModelWrapper(Object model) {
        this.model = model;

        Class<T> clazz = (Class<T>) model.getClass();
        this.descriptor = ModelDescriptorContext.INSTANCE.get(clazz);

        init();
    }

    private void init() {
        Collection<FieldDescriptor> fds = descriptor.fieldDescriptors();
        columnValueMapWithoutPK = new LinkedHashMap<>(fds.size());

        lock.lock();
        try {
            for (FieldDescriptor fd : fds) {
                if (fd.isPrimaryKey()) {
                    continue;
                }
                Object value = getValue(fd);
                columnValueMapWithoutPK.put(fd.getColumn(), value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }

    }

    public String getPrimaryKeyFieldName(){
        return descriptor.getPrimaryKeyFieldName();
    }

    public String getPrimaryKeyColumn(){
        return descriptor.getPrimaryKeyColumn();
    }

    public Object getPrimaryKeyValue() {
        return descriptor.getPrimaryKeyValue(model);
    }

    public String tableName() {
        return descriptor.tableName();
    }

    public Map<String, Object> columnValueMapWithoutPK() {
        return columnValueMapWithoutPK;
    }

    private Object getValue(FieldDescriptor fd) throws InvocationTargetException, IllegalAccessException {

        Method readMethod = fd.getReadMethod();
        if (null == readMethod) {
            return null;
        }
        Object value = fd.getReadMethod().invoke(model);
        if (null == value) {
            return null;
        }
        if (value instanceof EnumValue) {
            return ((EnumValue) value).getCode();
        }
        return value;
    }
}

package com.github.mybatis.ext.descriptor;

import com.github.mybatis.ext.exception.NoTableAnnotationFound;
import com.github.mybatis.ext.annotation.Table;
import com.github.mybatis.ext.exception.InternalException;
import com.github.mybatis.ext.exception.NoColumnAnnotationFound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class ModelDescriptor<T> {

    private Class<T> clazz;

    private String tableName;

    private List<FieldDescriptor> fieldDescriptors;

    private FieldDescriptor primaryKeyDescriptor;

    public ModelDescriptor(Class<T> clazz){
        this.clazz = clazz;

        Table table = clazz.getAnnotation(Table.class);
        if(null == table){
            throw new NoTableAnnotationFound(this.clazz);
        }
        tableName = table.value();

        this.fieldDescriptors = DescriptorUtil.fieldDescriptors(this.clazz);

        if(fieldDescriptors.isEmpty()){
            throw new NoColumnAnnotationFound(this.clazz);
        }

        primaryKeyDescriptor = fieldDescriptors.stream().filter(FieldDescriptor::isPrimaryKey).findFirst().orElse(null);

    }

    public String tableName(){
        return tableName;
    }

    public String getPrimaryKeyFieldName(){
        if(null == primaryKeyDescriptor){
            return null;
        }
        return primaryKeyDescriptor.getName();
    }

    public String getPrimaryKeyColumn(){
        if(null == primaryKeyDescriptor){
            return null;
        }
        return primaryKeyDescriptor.getColumn();
    }


    public Object getPrimaryKeyValue(Object obj){
        if(null == primaryKeyDescriptor){
            return null;
        }

        Method readMethod = primaryKeyDescriptor.getReadMethod();
        if(null == readMethod){
            return null;
        }
        try {
            return readMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalException(e.getMessage(), e);
        }
    }

    public Collection<FieldDescriptor> fieldDescriptors(){
        return fieldDescriptors;
    }

}

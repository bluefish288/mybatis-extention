package com.github.mybatis.ext.exception;


/**
 * @author xiebo
 */
public class NoColumnAnnotationFound extends InternalException{

    public NoColumnAnnotationFound(Class clazz) {
        super("no @Column annotation found on class "+clazz.getName());
    }
}
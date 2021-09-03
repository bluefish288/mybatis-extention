package com.github.mybatis.ext.support;

import com.github.mybatis.ext.support.CustomThreadLocal;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * @author xiebo
 */
public class Context {

    private final static ThreadLocal<Integer> PAGE_COUNT_HOLDER = new CustomThreadLocal<>();

    private final static ThreadLocal<MappedStatement> MAPPED_STATEMENT_HOLDER = new CustomThreadLocal<>();

    private final static ThreadLocal<List<Object>> INSERT_UPDATE_ARG_HOLDER = new CustomThreadLocal<>();

    private final static ThreadLocal<String> INSERT_PK = new CustomThreadLocal<>();

    public static void setPageCount(Integer pageCount){
        PAGE_COUNT_HOLDER.set(pageCount);
    }
    public static Integer getPageCount(){
        return PAGE_COUNT_HOLDER.get();
    }

    public static void setMappedStatement(MappedStatement mappedStatement){
        MAPPED_STATEMENT_HOLDER.set(mappedStatement);
    }
    public static MappedStatement getMappedStatement(){
        return MAPPED_STATEMENT_HOLDER.get();
    }

    public static void setInsertUpdateArgs(List<Object> args){
        INSERT_UPDATE_ARG_HOLDER.set(args);
    }
    public static List<Object> getInsertUpdateArgs(){
        return INSERT_UPDATE_ARG_HOLDER.get();
    }

    public static void setModelPKField(String modelPKField){
        INSERT_PK.set(modelPKField);
    }
    public static String getModelPKField(){
        return INSERT_PK.get();
    }

    public static void clear(){
        PAGE_COUNT_HOLDER.remove();
        MAPPED_STATEMENT_HOLDER.remove();
        INSERT_UPDATE_ARG_HOLDER.remove();
        INSERT_PK.remove();
    }

}

package com.github.mybatis.ext.intercept;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author xiebo
 */
public interface InterceptHandler {

    /**
     * Executor.query的拦截
     * @param ms
     * @param parameter
     * @throws Throwable
     */
    public default void onQueryOfExecutor(MappedStatement ms, Object parameter) throws Throwable{};

    /**
     * Executor.update的拦截
     * @param ms
     * @param parameter
     * @throws Throwable
     */
    public default void onUpdateOfExecutor(MappedStatement ms, Object parameter) throws Throwable{};

    /**
     * StatementHandler.prepare的拦截
     * @param statementHandler
     * @param connection
     * @throws Throwable
     */
    public default void onPrepareOfStatementHandler(StatementHandler statementHandler, Connection connection) throws Throwable{};

    /**
     * ParameterHandler.setParameter的拦截
     * @param parameterHandler
     * @param preparedStatement
     * @throws Throwable
     */
    public default void onSetParametersOfParameterHandler(ParameterHandler parameterHandler, PreparedStatement preparedStatement) throws Throwable{};

}
package com.github.mybatis.ext.intercept.handlers;

import com.github.mybatis.ext.intercept.AbstractInterceptHandler;
import com.github.mybatis.ext.page.PageParam;
import com.github.mybatis.ext.support.ReflectHelper;
import com.github.mybatis.ext.support.Context;
import com.github.mybatis.ext.support.SqlUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiebo
 */
public class PageInterceptHandler extends AbstractInterceptHandler {

    private final static Logger logger = LoggerFactory.getLogger(PageInterceptHandler.class);

    @Override
    public void onQueryOfExecutor(MappedStatement ms, Object parameter) throws Throwable {

        if(!(parameter instanceof PageParam)){
            return;
        }

        Context.setMappedStatement(ms);
    }

    @Override
    public void onPrepareOfStatementHandler(StatementHandler statementHandler, Connection connection) throws Throwable {

        MappedStatement mappedStatement = Context.getMappedStatement();

        if(null == mappedStatement){
            return;
        }

        BoundSql boundSql = statementHandler.getBoundSql();


        if(!(boundSql.getParameterObject() instanceof PageParam)){
            return;
        }

        PageParam pageParam = (PageParam) boundSql.getParameterObject();

        String selectSql = boundSql.getSql();
        logger.debug(selectSql);

        String countSql = SqlUtil.getCountSql(selectSql);
        logger.debug(countSql);


        int count = queryCount(mappedStatement, countSql, boundSql, pageParam, connection);

        if(pageParam.getCurrentPage() < 1){
            pageParam.setCurrentPage(1);
        }else{
            int totalPage = (count % pageParam.getPageSize() == 0) ? (count / pageParam.getPageSize()) : ((count / pageParam.getPageSize())+1);
            if(totalPage > 0){
                if(pageParam.getCurrentPage() > totalPage){
                    pageParam.setCurrentPage(totalPage);
                }
            }
        }

        int offset = (pageParam.getCurrentPage() - 1) * pageParam.getPageSize();

        selectSql += " limit " + offset + "," + pageParam.getPageSize();

        ReflectHelper.setValueByFieldName(boundSql, "sql", selectSql);

        Context.setPageCount(count);
    }

    private int queryCount(MappedStatement mappedStatement, String countSql, BoundSql boundSql, Object parameterObject, Connection connection) throws SQLException {
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);

        if(null!=boundSql.getParameterMappings()){
            for(ParameterMapping mapping : boundSql.getParameterMappings()){
                String prop = mapping.getProperty();
                if(boundSql.hasAdditionalParameter(prop)){
                    countBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }
        }

        MappedStatement countMappedStatement = buildMappedStatement(mappedStatement, new SqlSource() {
            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return countBoundSql;
            }
        }, mappedStatement.getId() + "_LIMIT", mappedStatement.getResultMaps());

        ParameterHandler parameterHandler = new DefaultParameterHandler(countMappedStatement, parameterObject, countBoundSql);

        PreparedStatement countStmt = connection.prepareStatement(countSql);

        parameterHandler.setParameters(countStmt);

        ResultSet rs = countStmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;

        rs.close();
        countStmt.close();

        Context.clear();
        return count;
    }
}
package com.github.mybatis.ext.intercept.handlers;

import com.github.mybatis.ext.AbstractDao;
import com.github.mybatis.ext.intercept.AbstractInterceptHandler;
import com.github.mybatis.ext.support.Context;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author xiebo
 */
public class InsertUpdateInterceptHandler extends AbstractInterceptHandler {

    @Override
    public void onUpdateOfExecutor(MappedStatement ms, Object parameter) throws Throwable {
        String sqlId = ms.getId();
        if(sqlId.endsWith(AbstractDao.INSERT_METHOD_NAME) || sqlId.endsWith(AbstractDao.UPDATE_METHOD_NAME)){
            Context.setMappedStatement(ms);
        }
    }

    @Override
    public void onSetParametersOfParameterHandler(ParameterHandler parameterHandler, PreparedStatement preparedStatement) throws Throwable {

        MappedStatement mappedStatement = Context.getMappedStatement();
        if(null == mappedStatement){
            return;
        }

        List<Object> insertArgs = Context.getInsertUpdateArgs();
        if (null == insertArgs || insertArgs.size() == 0) {
            return;
        }

        for (int i = 0; i < insertArgs.size(); i++) {
            preparedStatement.setObject(i + 1, insertArgs.get(i));
        }


        String pkFieldName = Context.getModelPKField();
        if(null!=pkFieldName && null!=mappedStatement.getKeyProperties() && mappedStatement.getKeyProperties().length  == 1){
            mappedStatement.getKeyProperties()[0] = pkFieldName;
        }

        Context.clear();
    }
}
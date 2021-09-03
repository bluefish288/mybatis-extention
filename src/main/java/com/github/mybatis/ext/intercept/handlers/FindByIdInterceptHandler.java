package com.github.mybatis.ext.intercept.handlers;

import com.github.mybatis.ext.exception.InternalException;
import com.github.mybatis.ext.AbstractDao;
import com.github.mybatis.ext.intercept.AbstractInterceptHandler;
import com.github.mybatis.ext.support.ReflectHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;

/**
 * @author xiebo
 */
public class FindByIdInterceptHandler extends AbstractInterceptHandler {

    @Override
    public void onQueryOfExecutor(MappedStatement ms, Object parameter) throws Throwable {

        String sqlId = ms.getId();

        if(!sqlId.endsWith(AbstractDao.FIND_BY_ID_METHOD)){
            return;
        }

        if(!(parameter instanceof MapperMethod.ParamMap)){
            return;
        }

        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;

        if(!paramMap.containsKey("clazz")){
            return;
        }

        Class cls = (Class) paramMap.get("clazz");

        if (null == cls) {
            return;
        }

        try{
            ResultMap originResultMap = ms.getResultMaps().get(0);

            ReflectHelper.setValueByFieldName(originResultMap,"type", cls);

        }catch (Exception e){
            throw new InternalException(e.getMessage(), e);
        }

    }
}
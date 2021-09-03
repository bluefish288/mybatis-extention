package com.github.mybatis.ext;

import com.github.mybatis.ext.enumdata.EnumValue;
import com.github.mybatis.ext.enumdata.EnumValueHandler;
import com.github.mybatis.ext.intercept.CustomInterceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author xiebo
 * @date 2021/9/2 5:54 下午
 */
public class SqlSessionFactoryUtil {

    public static void fill(SqlSessionFactory sqlSessionFactory){
        assert null!=sqlSessionFactory;
        Configuration configuration = sqlSessionFactory.getConfiguration();
        assert null!=configuration;

        configuration.setMapUnderscoreToCamelCase(true);
        configuration.getTypeHandlerRegistry().register(EnumValue.class, EnumValueHandler.class);
        configuration.addInterceptor(new CustomInterceptor());
    }
}

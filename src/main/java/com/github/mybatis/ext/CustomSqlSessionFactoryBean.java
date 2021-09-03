package com.github.mybatis.ext;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * @author xiebo
 */
public class CustomSqlSessionFactoryBean extends SqlSessionFactoryBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        SqlSessionFactoryUtil.fill(super.getObject());
    }
}
package com.github.mybatis.ext.descriptor;

import com.github.mybatis.ext.exception.InternalException;
import com.github.mybatis.ext.annotation.Column;
import com.github.mybatis.ext.support.StringUtils;
import com.github.mybatis.ext.Config;
import com.github.mybatis.ext.annotation.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DescriptorUtil {

    private final static String DEFAULT_ID_NAME = "id";

    private final static Logger logger = LoggerFactory.getLogger(DescriptorUtil.class);

    public static List<FieldDescriptor> fieldDescriptors(Class clazz){

        Field[] fields = clazz.getDeclaredFields();

        List<FieldDescriptor> descriptors = new ArrayList<>(fields.length);

        PropertyDescriptor pd = null;

        boolean primaryKeyFound = false;
        for (Field f : fields) {
            try {

                int modifiers = f.getModifiers();
                // 排除static和final
                if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }

                // 排除没有Column的字段

                Column col = f.getDeclaredAnnotation(Column.class);

                if(null == col && Config.INSTANCE.isUseColumnAnnotation()){
                    continue;
                }

                pd = new PropertyDescriptor(f.getName(), clazz);
                FieldDescriptor descriptor = new FieldDescriptor();

                descriptor.setName(pd.getName());

                if(null!=col && StringUtils.hasLength(col.value())){
                    descriptor.setColumn(col.value());
                }else{
                    descriptor.setColumn(underscoreName(pd.getName()));
                }

                if(!primaryKeyFound){
                    if(f.isAnnotationPresent(Id.class) || DEFAULT_ID_NAME.equals(pd.getName())){
                        descriptor.setPrimaryKey(true);
                        primaryKeyFound = true;
                    }
                }

                descriptor.setType(pd.getPropertyType());
                descriptor.setReadMethod(pd.getReadMethod());
                descriptor.setWriteMethod(pd.getWriteMethod());
                descriptors.add(descriptor);
            } catch (IntrospectionException e) {
                throw new InternalException(e.getMessage(), e);
            }
        }

        pd = null;

        return descriptors;
    }

    private static String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }
}
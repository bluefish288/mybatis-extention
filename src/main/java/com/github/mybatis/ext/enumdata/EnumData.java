package com.github.mybatis.ext.enumdata;




import com.github.mybatis.ext.exception.InternalException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiebo
 */
public class EnumData<T> {

    private Class enumClazz;

    private Map<T, EnumValue> enumValueMap = new ConcurrentHashMap<T, EnumValue>();

    public EnumData(Class enumClazz){
        this.enumClazz = enumClazz;

        if(!enumClazz.isEnum()){
            throw new InternalException("class "+enumClazz.getName()+" is not an enum");
        }

        for(Object obj : enumClazz.getEnumConstants()){
            if(obj instanceof EnumValue){
                EnumValue<T> enumValue = (EnumValue<T>) obj;
                enumValueMap.put(enumValue.getCode(),enumValue);
            }
        }

    }

    public EnumValue get(Object code){
        return enumValueMap.get(code);
    }

    @Override
    public String toString() {
        return "EnumData{" +
                "enumClazz=" + enumClazz +
                ", enumValueMap=" + enumValueMap +
                '}';
    }
}

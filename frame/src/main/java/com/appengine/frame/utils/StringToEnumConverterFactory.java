package com.appengine.frame.utils;
import com.alibaba.druid.util.StringUtils;
import com.appengine.frame.server.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class, Converter> converterMap =  new HashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter<String, T> converter = converterMap.get(targetType);
        if(converter == null) {
            converter = new StringToEnumConverter<>(targetType);
            converterMap.put(targetType, converter);
        }
        return converter;
    }

    static class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {

        private Map<String, T> enumMap = new HashMap<>();

        StringToEnumConverter(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            for(T e : enums) {
                enumMap.put(e.getValue(), e);
            }
        }

        @Override
        public T convert(String source) {

            T t = enumMap.get(source);
            if (t == null) {
                // 异常可以稍后去捕获
                throw new IllegalArgumentException("No element matches " + source);
            }
            return t;
        }
    }

//    @Override
//    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
//        return new StringToEnum(targetType);
//    }
//
//    private static class StringToEnum<T extends BaseEnum> implements Converter<String, T> {
//        private Class<T> targerType;
//        public StringToEnum(Class<T> targerType) {
//            this.targerType = targerType;
//        }
//
//        @Override
//        public T convert(String source) {
//            if (StringUtils.isEmpty(source)) {
//                return null;
//            }
//            return (T) StringToEnumConverterFactory.getIEnum(this.targerType, source);
//        }
//    }
//
//    public static <T extends BaseEnum> Object getIEnum(Class<T> targerType, String source) {
//        for (T enumObj : targerType.getEnumConstants()) {
//            if (source.equals(String.valueOf(enumObj.getValue()))) {
//                return enumObj;
//            }
//        }
//        return null;
//    }

//    private class StringToEnum<T extends SpringConverterableEnum> implements Converter<String, T> {
//
//        private final T[] values;
//
//        public StringToEnum(Class<T> targetType) {
//            values = targetType.getEnumConstants();
//        }
//
//        @Override
//        public T convert(String source) {
//            for (T t : values) {
//                if (t.getValue() .equals(source)) {
//                    return t;
//                }
//            }
//            return null;
//        }
//    }

    
}

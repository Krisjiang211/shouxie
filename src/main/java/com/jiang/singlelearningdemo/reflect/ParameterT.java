package com.jiang.singlelearningdemo.reflect;

import com.jiang.singlelearningdemo.common.pojo.User;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ParameterT {


    public static void main(String[] args) {
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();//获取字段上的所有注解
            String fieldName = field.getName();//获取字段名
            Class<?> fieldType = field.getType();//获取字段类型
        }
    }
}

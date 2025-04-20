package com.jiang.singlelearningdemo.moni;

import com.jiang.singlelearningdemo.common.pojo.User;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MybatisPlusLamdaQueryMoni {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        user.setName("jiang");
        user.setAge(16);
        Map<String, Object> map = addKV(user, User::getName);
        System.out.println("map = " + map);
    }



    private static <T,R> Map<String,Object> addKV(T t, SFun<T,R> func)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        SerializedLambda invoke = (SerializedLambda) writeReplace.invoke(func);
        String implMethodName = invoke.getImplMethodName();
        String paramName = implMethodName.substring(3,4).toLowerCase()+implMethodName.substring(4);
        R value =  func.apply(t);
        return Map.of(paramName,value);
    }
}

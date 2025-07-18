package com.jiang.singlelearningdemo.moni;

import com.jiang.singlelearningdemo.common.pojo.User;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MybatisPlusLamdaQueryMoni {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = getMethodName(User::getName);
        String paramName = getParamName(methodName);

        System.out.println("methodName = " + methodName);
        System.out.println("paramName = " + paramName);
    }



    private static <T,R> String getMethodName(SFun<T,R> func)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        SerializedLambda invoke = (SerializedLambda) writeReplace.invoke(func);
        return invoke.getImplMethodName();
    }
    private static String getParamName(String methodName){
        return methodName.substring(3,4).toLowerCase()+methodName.substring(4);
    }
}

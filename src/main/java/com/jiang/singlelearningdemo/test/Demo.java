package com.jiang.singlelearningdemo.test;

import com.jiang.singlelearningdemo.common.pojo.User;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Demo {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("orderIdOfUserId(123456789L) = " + orderIdOfUserId(12345678901L));

        User user = new User();
        Demo demo = new Demo();
        String ok = null;
        try {
            ok = demo.ok(User::getId);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ok = " + ok);
    }

    private String ok(SFunction<User,Object> fun) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method writeReplace = fun.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        SerializedLambda invoke =(SerializedLambda) writeReplace.invoke(fun);
        return invoke.getImplMethodName();
    }

    private static String orderIdOfUserId(Long userId){
        char[] res = new char[10];
        char[] charArray = String.valueOf(userId).toCharArray();
        if (charArray.length < 10) {
            for (int i=0;i<charArray.length;i++){
                res[i]=charArray[i];
            }
            for (int j = charArray.length; j < res.length; j++) {
                res[j]='0';
            }
            return String.valueOf(res);
        } else if (charArray.length==10){
            return String.valueOf(charArray);
        }else {

            for (int i = 9,j=charArray.length-1; i >= 0; i--,j--) {
                res[i]=charArray[j];
            }
            return String.valueOf(res);
        }
    }




}

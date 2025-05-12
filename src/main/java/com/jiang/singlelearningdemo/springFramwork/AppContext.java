package com.jiang.singlelearningdemo.springFramwork;

import com.jiang.singlelearningdemo.springFramwork.annotation.MyComponent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AppContext {

    public static void main(String[] args) throws IOException {
        new AppContext().initContext("com.jiang.singlelearningdemo.springFramwork.beans");
    }

    public void initContext(String packageName) throws IOException {
        for (Class<?> aClass : scanPackage(packageName)) {
            System.out.println("aClass.getName() = " + aClass.getName());
            try {
                Object o = aClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public List<Class<?>> scanPackage(String packageName) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = AppContext.class.getClassLoader();
        URL resource = classLoader.getResource(packageName.replace(".","/"));
        Path path = Path.of(resource.getFile().substring(1));//获取包的路径, 去掉第一个/. 适用于windows
        Files.walkFileTree(path,new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path absolutePath = file.toAbsolutePath();
                String fileName = absolutePath.toString();
                if (fileName.endsWith(".class")){
                    String absolutePathString = absolutePath.toString();//绝对路径
                    String midPath = absolutePathString.replace(File.separator, ".");//将文件路径替换为包名的形式
                    int i = midPath.indexOf(packageName);//找到包名的位置
                    String fullClassName = midPath.substring(i, midPath.length() - 6);
                    try {
                        Class<?> aClass = Class.forName(fullClassName);
                        if (hasAnnotation(aClass)){
                            classes.add(aClass);//将类添加到数组中
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classes;
    }




    public Object getBean(String name){
        return null;
    }


    public Object getBean(Class<?> clazz){
        return null;
    }



    private boolean hasAnnotation(Class<?> clazz){
        return clazz.isAnnotationPresent(MyComponent.class);
    }

}

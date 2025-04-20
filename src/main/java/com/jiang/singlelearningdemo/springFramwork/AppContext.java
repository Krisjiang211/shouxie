package com.jiang.singlelearningdemo.springFramwork;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.List;

public class AppContext {

    public static void main(String[] args) throws IOException {
        new AppContext().initContext("com.jiang.singlelearningdemo.springFramwork.beans");
    }

    public void initContext(String packageName) throws IOException {
        ClassLoader classLoader = AppContext.class.getClassLoader();
        URL resource = classLoader.getResource(packageName.replace(".","/"));
        Path path = Path.of(resource.getFile().substring(1));
        Files.walkFileTree(path,new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path absolutePath = file.toAbsolutePath();
                String fileName = absolutePath.toString();
                if (fileName.endsWith(".class")){
                    String absolutePathString = absolutePath.toString();
                    int i = absolutePathString.indexOf(packageName);
                    String fullClassName = absolutePathString.substring(i, absolutePathString.length() - 6);
                    System.out.println("fullClassName = " + fullClassName);
                    try {
                        Class<?> aClass = Class.forName(String.valueOf(absolutePath));
                        Object o = aClass.getConstructor().newInstance();
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }


    public List<Class<?>> scanPackage(String packageName){
        return null;
    }




    public Object getBean(String name){
        return null;
    }


    public Object getBean(Class<?> clazz){
        return null;
    }




}

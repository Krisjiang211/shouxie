package com.jiang.singlelearningdemo.proxy.core;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiang.singlelearningdemo.proxy.core.annotation.Param;
import com.jiang.singlelearningdemo.proxy.core.pojo.User;
import org.apache.ibatis.binding.MapperProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SqlSessionFactory {


    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},//代理的接口列表(这里表示只代理当前的接口)
                new FunInvokeHandler());
    }


}


class FunInvokeHandler implements InvocationHandler{
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "801219Abc***";


    private Connection connection;
    public FunInvokeHandler(){
        connection=getConnection();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith("select")) {
            String selectSql = createSelectSql(proxy, method, args);

        }
        return null;
    }

    private <T> T executeSql(String sql,Class<T> clazz) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return getRes(rs,clazz);
    }

    private <T> T getRes(ResultSet rs, Class<T> clazz) {
        try {
            T t = clazz.getConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(t, rs.getObject(field.getName()));
            }
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String createSelectSql(Object proxy, Method method, Object[] args) {
        StringBuilder sqlBuilder= new StringBuilder();
        sqlBuilder.append("SELECT ");
        String columns = String.join(",", getColumns(method));
        sqlBuilder.append(columns);
        sqlBuilder.append(" FROM ");
        String tableName = getTableName(method.getReturnType());
        sqlBuilder.append(tableName);
        sqlBuilder.append(" WHERE ");
        String whereClause = getWhereClause(method, args);
        sqlBuilder.append(whereClause);
        return sqlBuilder.toString();
    }

    private String getWhereClause(Method method,Object[] args) {
        StringBuilder sb = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Object arg = args[i];
            String paramStr = param.getAnnotation(Param.class).value();
            sb.append(paramStr+"="+arg);
            if (i < parameters.length - 1) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

    private String getTableName(Class<?> returnType) {
        TableName tableName = returnType.getAnnotation(TableName.class);
        return tableName.value();
    }

    //不支持驼峰映射的获取列名
    private List<String> getColumns(Method method) {
        ArrayList<String> list = new ArrayList<>();
        for (Field field : method.getReturnType().getDeclaredFields()) {
            list.add(field.getName());
        }
        return list;
    }
}
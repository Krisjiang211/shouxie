//package com.jiang.singlelearningdemo.mybatis;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Signature;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@Intercepts({
//        @Signature(type = Executor.class, method = "query",
//                args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})
//})
//public class QueryInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
//        Object parameter = invocation.getArgs()[1];
//        RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
//
//        ResultHandler myResHandler = context->{
//            System.out.println("context = " + context);
//            int resultCount = context.getResultCount();
//            Object resultObject = context.getResultObject();
//        };
//        invocation.getArgs()[3] = myResHandler;
//
//        Object sqlInvokedRes = invocation.proceed();
//        return sqlInvokedRes;
//
//
//    }
//}

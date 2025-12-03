package com.jiang.singlelearningdemo.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args={MappedStatement.class,Object.class})
})
public class UpdateInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        String id = mappedStatement.getId();//获取mappedStatement的id, 其实就是mapper.xml中定义的id
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();//获取Sql类型
        String sql = mappedStatement.getBoundSql(parameter).getSql();//实际执行的sql
        Object sqlInvokedRes;
        try {
             sqlInvokedRes = invocation.proceed();
        }catch (Throwable e){
            System.out.println("sql:"+sql+" 执行失败");
            throw e;
        }
        return sqlInvokedRes;
    }
}

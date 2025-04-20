package com.jiang.singlelearningdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private static final SpelExpressionParser parser=new SpelExpressionParser();


    @Around("@annotation(logMy)")
    public Object log(ProceedingJoinPoint joinPoint,Log logMy) throws Throwable {
        //获取方法的参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        StandardEvaluationContext context =new StandardEvaluationContext();
        for(int i=0;i< parameterNames.length;i++){
            context.setVariable(parameterNames[i],args[i]);
        }
        //存入并解析SpEL表达式
        Expression expression = parser.parseExpression(logMy.desc());
        String logContent = expression.getValue(context, String.class);

        long beforeT = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object res = joinPoint.proceed();
        long afterT = System.currentTimeMillis();
        log.info("{} 方法{}执行耗时：{}ms",logContent, methodName, afterT - beforeT);
        return res;
    }


}

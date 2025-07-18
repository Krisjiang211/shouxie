package com.jiang.singlelearningdemo.common.controller.advice;

import com.jiang.singlelearningdemo.common.util.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.text.SimpleDateFormat;
import java.util.Map;

@ControllerAdvice
public class ResAdvice implements ResponseBodyAdvice<Object> {

    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //全部拦截
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType().equals(Result.class)){
            Result<?> result = (Result<?>) body;
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }else if (returnType.getParameterType().equals(Map.class)){
            try {
                Map<Object,Object> map = (Map<Object,Object>) body;
                map.put("timestamp",System.currentTimeMillis());
                return map;
            }catch (Exception e){
                return body;
            }
        }
        return body;
    }
}

package com.jiang.singlelearningdemo.enhance;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.Map;


public class ReqBodyTimeResolver implements HandlerMethodArgumentResolver {

    private RequestResponseBodyMethodProcessor processor;

    private ApplicationContext applicationContext;

    public ReqBodyTimeResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ReqBodyTime.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (processor == null){
            initProcessor();
        }
        Object o = processor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (o instanceof Map<?,?>){
            Map o1 = (Map) o;
            o1.put("timestamp", System.currentTimeMillis());
            return o1;
        }
        return o;
    }

    public void initProcessor() {
        RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        for (HandlerMethodArgumentResolver argumentResolver : handlerAdapter.getArgumentResolvers()) {
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor){
                processor = (RequestResponseBodyMethodProcessor) argumentResolver;
            }
        }
    }

}

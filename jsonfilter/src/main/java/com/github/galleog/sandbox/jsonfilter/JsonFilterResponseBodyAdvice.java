package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link ResponseBodyAdvice} implementation that supports the {@link JsonFilter} annotation declared on a Spring MVC
 * {@code @RequestMapping} or {@code @ExceptionHandler} method.
 * <p/>
 * The created {@link ExceptPropertyFilter} is used within {@link MappingJackson2HttpMessageConverter} to serialize
 * the response body to JSON.
 *
 * @author Oleg Galkin
 * @see JsonFilter
 * @see ExceptPropertyFilter
 * @see MappingJackson2HttpMessageConverter
 */
@ControllerAdvice
public class JsonFilterResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType) &&
                (returnType.getMethodAnnotation(JsonFilter.class) != null ||
                        returnType.getMethodAnnotation(JsonFilters.class) != null);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
        ExceptPropertyFilter filter;
        JsonFilter annotation = returnType.getMethodAnnotation(JsonFilter.class);
        if (annotation != null) {
            filter = new ExceptPropertyFilter(annotation);
        } else {
            filter = new ExceptPropertyFilter(returnType.getMethodAnnotation(JsonFilters.class).value());
        }

        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter(JsonFilterAnnotationIntrospector.DEFAULT_FILTER_ID, filter);
        bodyContainer.setFilters(filters);
    }
}

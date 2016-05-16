package server.service.rest_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import transferFiles.exceptions.ExceptionsWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Autowired
    ObjectMapper mapper;

    public MyHandlerExceptionResolver() {
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            response.addHeader("exception", mapper.writeValueAsString(new ExceptionsWrapper(ex.getClass(), ex.getMessage())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}

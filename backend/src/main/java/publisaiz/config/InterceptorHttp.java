package publisaiz.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
class InterceptorHttp extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(InterceptorHttp.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        logger.info("\nREQUEST: \n\n[{}]", request);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        logger.info("\nRESPONSE: \n\n[{}]", response);
    }
}
package com.bingo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemInterceptHandler extends HandlerInterceptorAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(SystemInterceptHandler.class);

    /**
     * @description 前置处理器，在请求处理之前调用
     * @param request
     * @param response
     * @param handler
     * @return Boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("前置处理器，在请求处理之前调用"+request.getRequestURI());
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}

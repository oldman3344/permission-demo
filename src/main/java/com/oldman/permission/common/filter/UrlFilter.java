package com.oldman.permission.common.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author oldman
 * @date 2021/8/16 15:59
 */
@Component
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String path=httpRequest.getRequestURI();
        if(path.indexOf("/api/v2")>-1){
            path = path.replaceAll("/api/v2","");
            httpRequest.getRequestDispatcher(path).forward(servletRequest,servletResponse);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
        return;
    }

    @Override
    public void destroy() {
    }
}

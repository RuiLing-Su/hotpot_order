package com.p.order.filter;

import com.alibaba.fastjson.JSON;
import com.p.order.common.BaseContext;
import com.p.order.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String requestURI = request.getRequestURI();
        log.info("拦截到{}请求",requestURI);

        String[] strings = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(strings,requestURI);

        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户ID为{}",request.getSession().getAttribute("employee"));

            Long employee = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employee);

            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户ID为{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param strings
     * @param requestURI
     * @return
     */
    private boolean check(String[] strings, String requestURI) {
        for (String s : strings) {
            boolean match = antPathMatcher.match(s, requestURI);
            if(match){
                return true;
            }
        }

        return false;
    }
    }



package com.oldman.permission.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.jwt.JwtUtils;
import com.oldman.permission.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 自定义JWT拦截器
 *
 * @author oldman
 * @date 2021/8/15 2:49
 */
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil (RedisUtil redisUtil){
        JwtTokenInterceptor.redisUtil= redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        NormalResponse rep = JwtUtils.verity(redisUtil);
        if (0 == rep.getCode()) {
            return true;
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            JSONObject res = new JSONObject();
            res.put("code", rep.getCode());
            res.put("msg", rep.getMsg());
            out = response.getWriter();
            out.append(res.toString());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
    }
}

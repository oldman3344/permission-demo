package com.oldman.permission.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.redis.RedisUtil;
import com.oldman.permission.common.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author oldman
 */
public class JwtUtils {
    /**
     * 有效期 60 * 60 * 1000 一个小时 (604800000L 一周7天)（单位：秒）
     */
    public static final Long EXPIRATION = 3600000L;
    /**
     * 密钥
     */
    private static final String SECRET = "asurplus_secret";

    /**
     * 生成用户token,设置token超时时间
     *
     * @param userId
     * @param claim
     * @return
     */
    public static String createToken(Long userId, String claim) {
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                // 添加头部
                .withHeader(map)
                // 放入用户的id
                .withAudience(String.valueOf(userId))
                // 可以将基本信息放到claims中
                .withClaim("claim", claim)
                // 超时设置,设置过期的日期
                //.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                // 签发时间
                .withIssuedAt(new Date())
                // SECRET加密
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 获取用户id
     */
    public static Integer getUserId(RedisUtil redisUtil) {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 从请求头部中获取token信息
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (null == redisUtil.get(token)){
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (null != jwt) {
                // 拿到我们放置在token中的信息
                List<String> audience = jwt.getAudience();
                if (null != audience && audience.size() > 0) {
                    return Integer.parseInt(audience.get(0));
                }
            }
        } catch (IllegalArgumentException | JWTVerificationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验token并解析token
     */
    public static NormalResponse verity(RedisUtil redisUtil) {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 从请求头部中获取token信息
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return new NormalResponse(Code.TOKEN_INVALID,"用户信息已过期，请重新登录");
        }
        if (null == redisUtil.get(token)){
            return new NormalResponse(Code.TOKEN_INVALID,"用户信息已过期，请重新登录");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (null != jwt) {
                // 拿到我们放置在token中的信息
                List<String> audience = jwt.getAudience();
                if (null != audience && audience.size() > 0) {
                    return new NormalResponse<String>(Code.SUCCESS,"认证成功").setData(audience.get(0));
                }
            }
        } catch (IllegalArgumentException | JWTVerificationException e) {
            e.printStackTrace();
        }
        return new NormalResponse(Code.TOKEN_INVALID,"用户信息已过期，请重新登录");
    }

    /*public static void main(String[] args) {
     *//*String jwt = JwtUtils.createJwt("weiyibiaoshi", "aaaaaa", null);
        System.out.println(jwt);*//*
        try {
            Claims claims = JwtUtils.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5YzZiYmI2My1jYTRkLTQ5MTUtOGE2NC1mZGRiZGQxNTkwM2IiLCJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwidGVzdDJcIn0iLCJpc3MiOiJvbGRtYW4iLCJpYXQiOjE2Mjg5NjY0MDh9.7Wpb2IuyAXGxEoUben0hKPTeyCnsYsZe5Lw9tDtgh3k");
            System.out.println(claims);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }*/
}

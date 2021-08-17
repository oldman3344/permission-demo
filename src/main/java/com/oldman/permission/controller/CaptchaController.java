package com.oldman.permission.controller;

import com.alibaba.fastjson.JSONObject;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.redis.RedisUtil;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author oldman
 * @date 2021/8/16 16:43
 */
@CrossOrigin
@RestController
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public NormalResponse captcha(HttpServletRequest request, HttpServletResponse response) {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为60s
        redisUtil.set("captcha:" + key, verCode, 60);
        JSONObject obj = new JSONObject();
        obj.put("key", key);
        obj.put("image", specCaptcha.toBase64());
        return new NormalResponse<JSONObject>(Code.SUCCESS).setData(obj);
    }
}

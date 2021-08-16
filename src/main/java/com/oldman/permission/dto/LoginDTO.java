package com.oldman.permission.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/14 14:08
 */
@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 159156870040660319L;

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String code;
    private String verKey;
}

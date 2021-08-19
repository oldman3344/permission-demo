package com.oldman.permission.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/19 21:54
 */
@Data
public class FindUserListDTO implements Serializable {
    private static final long serialVersionUID = -4608573032171118905L;

    private String username;
    private String nickname;
    private Integer sex;
    @NotNull(message = "页码不能为空")
    private Integer page;
    @NotNull(message = "每页数量不能为空")
    private Integer limit;
}

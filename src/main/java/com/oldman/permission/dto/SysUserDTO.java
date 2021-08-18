package com.oldman.permission.dto;

import com.oldman.permission.common.valid.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author oldman
 * @date 2021/8/14 14:21
 */
@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1879442726617268504L;

    @NotNull(message = "用户ID不能为空", groups = ValidGroup.Crud.Update.class)
    private Long id;
    @NotNull(message = "是否冻结账号不能为空", groups = ValidGroup.Crud.Update.class)
    private Boolean freeze;

    @NotBlank(message = "用户名不能为空", groups = ValidGroup.Crud.Create.class)
    private String username;
    @NotBlank(message = "密码不能为空", groups = ValidGroup.Crud.Create.class)
    private String password;
    @NotBlank(message = "性别不能为空", groups = ValidGroup.Crud.Create.class)
    private Integer sex;
    @NotBlank(message = "昵称不能为空", groups = ValidGroup.Crud.Create.class)
    private String nickname;
    private String phone;

    @NotNull(message = "当前页码不能为空", groups = ValidGroup.Crud.Query.class)
    private Integer page;
    @NotNull(message = "显示数量不能为空", groups = ValidGroup.Crud.Query.class)
    private Integer limit;

    @NotNull(message = "用户ID列表不能为空",groups = ValidGroup.Crud.Delete.class)
    private List<Integer> ids;
}

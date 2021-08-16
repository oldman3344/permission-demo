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
    @NotNull(message = "状态不能为空", groups = ValidGroup.Crud.Update.class)
    private Integer state;

    @NotBlank(message = "用户名不能为空", groups = ValidGroup.Crud.Create.class)
    private String username;
    @NotBlank(message = "密码不能为空", groups = ValidGroup.Crud.Create.class)
    private String password;

    @NotNull(message = "当前页码不能为空", groups = ValidGroup.Crud.Query.class)
    private Integer current;
    @NotNull(message = "显示数量不能为空", groups = ValidGroup.Crud.Query.class)
    private Integer size;

    @NotNull(message = "用户ID列表不能为空",groups = ValidGroup.Crud.Delete.class)
    private List<Integer> ids;
}

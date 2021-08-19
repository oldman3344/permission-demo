package com.oldman.permission.dto;

import com.oldman.permission.common.valid.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/14 14:21
 */
@Data
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 6413113158970772489L;

    @NotNull(message = "用户ID不能为空", groups = ValidGroup.Crud.Update.class)
    private Long id;

    @NotBlank(message = "用户名不能为空", groups = {ValidGroup.Crud.Create.class,ValidGroup.Crud.Update.class})
    private String username;
    @NotBlank(message = "密码不能为空", groups = ValidGroup.Crud.Create.class)
    private String password;
    @NotNull(message = "性别不能为空", groups = {ValidGroup.Crud.Create.class,ValidGroup.Crud.Update.class})
    private Integer sex;
    @NotBlank(message = "昵称不能为空", groups = {ValidGroup.Crud.Create.class,ValidGroup.Crud.Update.class})
    private String nickname;
    @NotNull(message = "角色不能为空", groups = {ValidGroup.Crud.Create.class,ValidGroup.Crud.Update.class})
    private Long[] roleIds;
    private String phone;
}

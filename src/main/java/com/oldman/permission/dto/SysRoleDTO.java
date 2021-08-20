package com.oldman.permission.dto;

import com.oldman.permission.common.valid.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/20 16:18
 */
@Data
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = -2490583330149136603L;

    @NotNull(message = "角色ID不能为空",groups = ValidGroup.Crud.Update.class)
    private Long id;

    @NotBlank(message = "角色名称不能为空",groups = {ValidGroup.Crud.Update.class,ValidGroup.Crud.Create.class})
    private String roleName;
    @NotBlank(message = "角色标识不能为空",groups = {ValidGroup.Crud.Update.class,ValidGroup.Crud.Create.class})
    private String roleCode;
    private String remark;
}

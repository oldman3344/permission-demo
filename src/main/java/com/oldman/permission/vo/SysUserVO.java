package com.oldman.permission.vo;

import com.oldman.permission.common.annotation.BindEntity;
import com.oldman.permission.common.annotation.BindField;
import com.oldman.permission.pojo.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/18 15:41
 */
@Data
@BindEntity(SysUser.class)
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 596467396123021401L;

    @BindField("id")
    private Long id;
    @BindField("username")
    private String username;
    @BindField("nickname")
    private String nickname;
    @BindField("phone")
    private String phone;
    @BindField("gmtCreate")
    private Long gmtCreate;
    @BindField("sex")
    private Integer sex;

    private String sexName;
}

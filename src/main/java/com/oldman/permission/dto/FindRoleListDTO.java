package com.oldman.permission.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author oldman
 * @date 2021/8/20 15:22
 */
@Data
public class FindRoleListDTO implements Serializable {
    private static final long serialVersionUID = -6980876064850105528L;

    private String roleName;
    private String roleCode;
    private String remark;
    private Integer page;
    private Integer limit;
}

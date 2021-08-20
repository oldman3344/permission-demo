package com.oldman.permission.service;

import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.dto.SysRoleDTO;
import com.oldman.permission.dto.FindRoleListDTO;
import com.oldman.permission.pojo.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
public interface SysRoleService extends IService<SysRole> {

    NormalResponse findRoleList(FindRoleListDTO dto, String... select);

    NormalResponse findRole(FindRoleListDTO dto);

    NormalResponse addRole(SysRoleDTO dto);

    NormalResponse deleteRole(Long id);

    NormalResponse deleteBatchRole(Long[] id);

    NormalResponse getRoleMenu(Long id);
}

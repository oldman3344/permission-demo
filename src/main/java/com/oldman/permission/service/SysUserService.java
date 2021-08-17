package com.oldman.permission.service;

import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.dto.SysUserDTO;
import com.oldman.permission.dto.LoginDTO;
import com.oldman.permission.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
public interface SysUserService extends IService<SysUser> {

    NormalResponse login(LoginDTO dto);

    NormalResponse addUser(SysUserDTO dto);

    NormalResponse updateUser(SysUserDTO dto);

    NormalResponse deleteUser(SysUserDTO dto);

    NormalResponse findUserList(SysUserDTO dto,String username);

    NormalResponse resetPassword(Integer id);
}

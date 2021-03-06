package com.oldman.permission.service;

import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.dto.FindUserListDTO;
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

    NormalResponse findUser(String username);

    NormalResponse addUser(SysUserDTO dto);

    NormalResponse deleteUser(Long id);

    NormalResponse deleteBatchUser(Long[] id);

    NormalResponse findUserList(FindUserListDTO dto);

    NormalResponse resetPassword(Integer id);

    NormalResponse updateState(Integer id,Integer state);
}

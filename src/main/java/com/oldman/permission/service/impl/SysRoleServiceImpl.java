package com.oldman.permission.service.impl;

import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.pojo.SysRole;
import com.oldman.permission.mapper.SysRoleMapper;
import com.oldman.permission.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public NormalResponse findRoleList() {
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        return new NormalResponse(Code.SUCCESS).setData(sysRoleList);
    }
}

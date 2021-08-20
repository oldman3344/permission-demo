package com.oldman.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.util.AssertUtils;
import com.oldman.permission.dto.SysRoleDTO;
import com.oldman.permission.dto.FindRoleListDTO;
import com.oldman.permission.mapper.SysMenuMapper;
import com.oldman.permission.mapper.SysRoleMenuMapper;
import com.oldman.permission.mapper.SysUserRoleMapper;
import com.oldman.permission.pojo.SysMenu;
import com.oldman.permission.pojo.SysRole;
import com.oldman.permission.mapper.SysRoleMapper;
import com.oldman.permission.pojo.SysRoleMenu;
import com.oldman.permission.pojo.SysUserRole;
import com.oldman.permission.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public NormalResponse findRoleList(FindRoleListDTO dto, String... select) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        List<SysRole> sysRoleList;
        if (null != dto) {
            if (StringUtils.isNotBlank(dto.getRoleName())) {
                wrapper.like("role_name", dto.getRoleName());
            }
            if (StringUtils.isNotBlank(dto.getRoleCode())) {
                wrapper.like("role_code", dto.getRoleCode());
            }
            if (StringUtils.isNotBlank(dto.getRemark())) {
                wrapper.like("remark", dto.getRemark());
            }
        } else {
            wrapper.select(select);
        }
        sysRoleList = sysRoleMapper.selectList(wrapper);
        return new NormalResponse<List<SysRole>>(Code.SUCCESS).setData(sysRoleList);
    }

    @Override
    public NormalResponse findRole(FindRoleListDTO dto) {
        SysRole sysRole;
        if (StringUtils.isNotBlank(dto.getRoleName()) && StringUtils.isBlank(dto.getRoleCode())) {
            sysRole = this.getSysRole("role_name", dto.getRoleName());
            if (null == sysRole) {
                return new NormalResponse(Code.ARGUMENT_ERROR);
            }
            return new NormalResponse(Code.SUCCESS).setData(sysRole.getRoleName());
        }
        sysRole = this.getSysRole("role_code", dto.getRoleCode());
        if (null == sysRole) {
            return new NormalResponse(Code.ARGUMENT_ERROR);
        }
        return new NormalResponse(Code.SUCCESS).setData(sysRole.getRoleCode());
    }

    @Override
    public NormalResponse addRole(SysRoleDTO dto) {
        SysRole sysRole;
        if (null == dto.getId()) {
            sysRole = new SysRole();
            sysRole.setRoleName(dto.getRoleName());
            sysRole.setRoleCode(dto.getRoleCode());
            if (StringUtils.isNotBlank(dto.getRemark())){
                sysRole.setRemark(dto.getRemark());
            }
            int insert = sysRoleMapper.insert(sysRole);
            AssertUtils.isTrue(insert < 1, "添加失败", Code.FAIL);
            return new NormalResponse(Code.SUCCESS, "添加成功");
        }
        sysRole = sysRoleMapper.selectById(dto.getId());
        sysRole.setRoleName(dto.getRoleName());
        sysRole.setRoleCode(dto.getRoleCode());
        sysRole.setRemark(dto.getRemark());
        int update = sysRoleMapper.updateById(sysRole);
        AssertUtils.isTrue(update < 1, "修改失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "修改成功");
    }

    @Override
    public NormalResponse deleteRole(Long id) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",id);
        int count = sysUserRoleMapper.selectCount(wrapper);
        if (count>0){
            return new NormalResponse(Code.FAIL,"删除失败，请先解除已绑用户");
        }
        int delete = sysRoleMapper.deleteById(id);
        AssertUtils.isTrue(delete < 1, "删除失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS,"删除成功");
    }

    @Override
    public NormalResponse deleteBatchRole(Long[] id) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.in("role_id",id);
        int count = sysUserRoleMapper.selectCount(wrapper);
        if (count>0){
            return new NormalResponse(Code.FAIL,"删除失败，请先解除已绑用户");
        }
        int deleteBatch = sysRoleMapper.deleteBatchIds(Arrays.asList(id));
        AssertUtils.isTrue(deleteBatch < id.length, "删除失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS,"删除成功");
    }

    @Override
    public NormalResponse getRoleMenu(Long id) {
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",id);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(wrapper);
        List<Long> collect = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        List<SysMenu> sysMenuList = sysMenuMapper.selectBatchIds(collect);
        /*List<SysMenu> oneMenus = sysMenuList.stream().filter(sysMenu -> {
            if (sysMenu.getParentId() == 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        List<SysMenu> childMenus = sysMenuList.stream().filter(sysMenu -> {
            if (sysMenu.getParentId() > 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        oneMenus.forEach(one->{
            childMenus.forEach(two ->{
                if (one.getId().equals(two.getParentId())){

                }
            });
        });*/

        return new NormalResponse(Code.SUCCESS).setData(sysMenuList.toArray());
    }

    private SysRole getSysRole(String field, String val) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq(field, val);
        SysRole sysRole = sysRoleMapper.selectOne(wrapper);
        return sysRole;
    }
}

package com.oldman.permission.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.mapper.SysRoleMenuMapper;
import com.oldman.permission.mapper.SysUserMapper;
import com.oldman.permission.mapper.SysUserRoleMapper;
import com.oldman.permission.pojo.SysMenu;
import com.oldman.permission.mapper.SysMenuMapper;
import com.oldman.permission.pojo.SysRoleMenu;
import com.oldman.permission.pojo.SysUser;
import com.oldman.permission.pojo.SysUserRole;
import com.oldman.permission.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    private JSONObject findOneMenuList(List<Long> menuIds) {
        JSONArray array = new JSONArray();
        menuIds.forEach(id -> {
            SysMenu sysMenu = sysMenuMapper.selectById(id);
            array.add(sysMenu);
        });
        JSONArray oneMenuArray = array.stream().filter(menu -> {
            SysMenu sysMenu = (SysMenu) menu;
            //false代表是菜单，0代表是一级菜单
            if (sysMenu.getHide() == false && sysMenu.getParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toCollection(JSONArray::new));
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("oneMenuList", oneMenuArray);

        JSONArray childMenuArray = array.stream().filter(menu -> {
            SysMenu sysMenu = (SysMenu) menu;
            //false代表是菜单，0代表是一级菜单，大于0代表子级菜单
            if (sysMenu.getHide() == false && sysMenu.getParentId() > 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toCollection(JSONArray::new));
        jsonObj.put("childMenuList", childMenuArray);
        return jsonObj;
    }

    @Override
    public NormalResponse findMenuList(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        SysUserRole sysUserRole = sysUserRoleMapper.selectById(sysUser.getId());
        QueryWrapper<SysRoleMenu> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("role_id", sysUserRole.getRoleId());
        List<Long> menuIds = sysRoleMenuMapper.selectList(wrapper01).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        JSONObject object = this.findOneMenuList(menuIds);
        JSONArray array = new JSONArray();
        ((JSONArray) object.get("oneMenuList")).forEach(obj01->{
            SysMenu menu01 = (SysMenu) obj01;
            JSONObject parent = new JSONObject();
            parent.put("title", menu01.getTitle());
            parent.put("icon", menu01.getIcon());
            parent.put("path", menu01.getPath());
            parent.put("hide", menu01.getHide());
            JSONArray childrenArray = new JSONArray();

            ((JSONArray) object.get("childMenuList")).forEach(obj02->{
                SysMenu menu02 = (SysMenu) obj02;
                if (menu02.getParentId().equals(menu01.getId())){
                    JSONObject children = new JSONObject();
                    children.put("title", menu02.getTitle());
                    children.put("icon", menu02.getIcon());
                    children.put("path", menu02.getPath());
                    children.put("component", menu02.getComponent());
                    children.put("hide", menu02.getHide());
                    children.put("hideFooter", menu02.getHideFooter());
                    children.put("hideSidebar", menu02.getHideSidebar());
                    children.put("closable", menu02.getClosable());
                    children.put("tabUnique", menu02.getTabUnique());
                    childrenArray.add(children);
                }
            });
            parent.put("children", childrenArray);
            array.add(parent);
        });
        return new NormalResponse<JSONArray>(Code.SUCCESS).setData(array);
    }
}

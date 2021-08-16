package com.oldman.permission.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.pojo.SysMenu;
import com.oldman.permission.mapper.SysMenuMapper;
import com.oldman.permission.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findOneMenuList() {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0).eq("is_hide",0);
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(wrapper);
        return sysMenuList;
    }

    @Override
    public List<SysMenu> findChildrenMenuList() {
        List<Long> oneIds = this.findOneMenuList().stream().map(SysMenu::getId).collect(Collectors.toList());
        JSONObject parent = new JSONObject();
        parent.put("title","");
        parent.put("icon","");
        parent.put("path","");
        parent.put("hide","");
        JSONArray childrenArray = new JSONArray();
        for (Long id : oneIds) {
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", id);
            List<SysMenu> list = sysMenuMapper.selectList(wrapper);
            JSONObject children = new JSONObject();
            children.put("title","");
            children.put("icon","");
            children.put("path","");
            children.put("component","");
            children.put("hide","");
            children.put("active","");
            children.put("hideFooter","");
            children.put("hideSidebar","");
            children.put("closable","");
            children.put("tabUnique","");
            childrenArray.add(children);

        }
        parent.put("children",childrenArray);
        return null;
    }
}

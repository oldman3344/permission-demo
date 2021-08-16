package com.oldman.permission.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldman.permission.common.Code;
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
    public NormalResponse findChildrenMenuList() {
        List<SysMenu> oneMenuList = this.findOneMenuList();
        JSONArray array = new JSONArray();
        for(SysMenu menu: oneMenuList){
            JSONObject parent = new JSONObject();
            parent.put("title",menu.getTitle());
            parent.put("icon",menu.getIcon());
            parent.put("path",menu.getPath());
            parent.put("hide",menu.getHide());
            JSONArray childrenArray = new JSONArray();
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", menu.getId());
            List<SysMenu> list = sysMenuMapper.selectList(wrapper);
            for (SysMenu child : list) {
                JSONObject children = new JSONObject();
                children.put("title",child.getTitle());
                children.put("icon",child.getIcon());
                children.put("path",child.getPath());
                children.put("hide",child.getHide());
                children.put("hideFooter",child.getHideFooter());
                children.put("hideSidebar",child.getHideSidebar());
                children.put("closable",child.getClosable());
                children.put("tabUnique",child.getTabUnique());
                childrenArray.add(children);
            }
            parent.put("children",childrenArray);
            array.add(parent);
        }
        return new NormalResponse<JSONArray>(Code.SUCCESS).setData(array);
    }
}

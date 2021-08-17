package com.oldman.permission.service;

import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.pojo.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
public interface SysMenuService extends IService<SysMenu> {
    NormalResponse findMenuList(Integer id);
}

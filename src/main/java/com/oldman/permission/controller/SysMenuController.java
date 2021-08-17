package com.oldman.permission.controller;


import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/getMenu")
    public NormalResponse getMenu(Integer id){
        return sysMenuService.findMenuList(id);
    }
}


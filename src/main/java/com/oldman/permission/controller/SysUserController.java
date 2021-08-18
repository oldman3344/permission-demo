package com.oldman.permission.controller;


import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.valid.ValidGroup;
import com.oldman.permission.dto.SysUserDTO;
import com.oldman.permission.dto.LoginDTO;
import com.oldman.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public NormalResponse login(@RequestBody @Valid LoginDTO dto){
        return sysUserService.login(dto);
    }

    @PostMapping("/addUser")
    public NormalResponse addUser(@RequestBody @Validated(value = ValidGroup.Crud.Create.class) SysUserDTO dto){
        return sysUserService.addUser(dto);
    }

    @PostMapping("/updateUser")
    public NormalResponse updateUser(@RequestBody @Validated(value = ValidGroup.Crud.Update.class) SysUserDTO dto){
        return sysUserService.updateUser(dto);
    }

    @GetMapping("/page")
    public NormalResponse findUserList(@Validated(value = ValidGroup.Crud.Query.class) SysUserDTO dto,String username){
        return sysUserService.findUserList(dto,username);
    }

    @DeleteMapping("/deleteUser")
    public NormalResponse deleteUser(@Validated(value = ValidGroup.Crud.Delete.class) SysUserDTO dto){
        return sysUserService.deleteUser(dto);
    }

    @PostMapping("/resetPassword")
    public NormalResponse resetPassword(Integer id){
        return sysUserService.resetPassword(id);
    }
}


package com.oldman.permission.controller;


import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.valid.ValidGroup;
import com.oldman.permission.dto.SysUserDTO;
import com.oldman.permission.dto.LoginDTO;
import com.oldman.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@RestController
@RequestMapping("/sys")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/user/login")
    public NormalResponse login(@RequestBody @Valid LoginDTO dto){
        return sysUserService.login(dto);
    }

    @GetMapping("/user")
    public NormalResponse findUser(String username){
        return sysUserService.findUser(username);
    }

    @PostMapping("/user")
    public NormalResponse addUser(@RequestBody @Validated(value = ValidGroup.Crud.Create.class) SysUserDTO dto){
        return sysUserService.addUser(dto);
    }

    @PutMapping("/user")
    public NormalResponse updateUser(@RequestBody @Validated(value = ValidGroup.Crud.Update.class) SysUserDTO dto){
        return sysUserService.updateUser(dto);
    }

    @DeleteMapping("/user/{id}")
    public NormalResponse deleteUser(@PathVariable Integer id){
        return sysUserService.deleteUser(id);
    }

    @GetMapping("/user/page")
    public NormalResponse findUserList(@Validated(value = ValidGroup.Crud.Query.class) SysUserDTO dto,String username){
        return sysUserService.findUserList(dto,username);
    }

    @PutMapping("/user/state/{id}/{state}")
    public NormalResponse updateState(@PathVariable Integer id,@PathVariable Integer state){
        return sysUserService.updateState(id,state);
    }

    @DeleteMapping("/user/batch")
    public NormalResponse deleteBatchUser(@RequestBody Integer[] id){
        return sysUserService.deleteBatchUser(id);
    }

    @PutMapping("/user/resetPassword/{id}")
    public NormalResponse resetPassword(@PathVariable Integer id){
        return sysUserService.resetPassword(id);
    }
}


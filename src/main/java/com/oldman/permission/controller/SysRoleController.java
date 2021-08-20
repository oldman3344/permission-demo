package com.oldman.permission.controller;


import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.valid.ValidGroup;
import com.oldman.permission.dto.SysRoleDTO;
import com.oldman.permission.dto.FindRoleListDTO;
import com.oldman.permission.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/role/page")
    public NormalResponse findRoleAllList(FindRoleListDTO dto){
        return sysRoleService.findRoleList(dto);
    }

    @GetMapping("/role")
    public NormalResponse findRoleList(FindRoleListDTO dto){
        return sysRoleService.findRoleList(null,"id","role_name");
    }

    @GetMapping("/role/verify")
    public NormalResponse verify(FindRoleListDTO dto){
        return sysRoleService.findRole(dto);
    }

    @PutMapping("/role")
    public NormalResponse updateRole(@RequestBody @Validated(value = ValidGroup.Crud.Update.class) SysRoleDTO dto){
        return sysRoleService.addRole(dto);
    }

    @PostMapping("/role")
    public NormalResponse addRole(@RequestBody @Validated(value = ValidGroup.Crud.Create.class) SysRoleDTO dto){
        return sysRoleService.addRole(dto);
    }

    @DeleteMapping("/role/{id}")
    public NormalResponse deleteRole(@PathVariable Long id){
        return sysRoleService.deleteRole(id);
    }

    @DeleteMapping("/role/batch")
    public NormalResponse deleteBatchRole(@RequestBody Long[] id){
        return sysRoleService.deleteBatchRole(id);
    }

    @GetMapping("/role/menu")
    public NormalResponse getRoleMenu(Long id){
        return sysRoleService.getRoleMenu(id);
    }
}


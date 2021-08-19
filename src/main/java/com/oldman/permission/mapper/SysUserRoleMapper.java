package com.oldman.permission.mapper;

import com.oldman.permission.pojo.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Results(id ="userRoleResults", value={
            @Result(property="id", column="id"),
            @Result(property="userId", column="user_id"),
            @Result(property="roleId",  column="role_id"),
    })
    @Select("select id,user_id,role_id from sys_user_role where user_id = #{id}")
    List<SysUserRole> findUserRoleList(Long id);
}

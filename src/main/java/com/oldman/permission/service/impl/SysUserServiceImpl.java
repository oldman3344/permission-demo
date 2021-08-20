package com.oldman.permission.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.jwt.JwtUtils;
import com.oldman.permission.common.redis.RedisUtil;
import com.oldman.permission.common.util.ArrayUtils;
import com.oldman.permission.common.util.AssertUtils;
import com.oldman.permission.dto.FindUserListDTO;
import com.oldman.permission.dto.SysUserDTO;
import com.oldman.permission.dto.LoginDTO;
import com.oldman.permission.mapper.SysDictMapper;
import com.oldman.permission.mapper.SysRoleMapper;
import com.oldman.permission.mapper.SysUserRoleMapper;
import com.oldman.permission.pojo.SysDict;
import com.oldman.permission.pojo.SysRole;
import com.oldman.permission.pojo.SysUser;
import com.oldman.permission.mapper.SysUserMapper;
import com.oldman.permission.pojo.SysUserRole;
import com.oldman.permission.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
@Transactional(rollbackFor = Exception.class)
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${cache.time}")
    private Integer CACHE_TIME;

    @Override
    public NormalResponse login(LoginDTO dto) {
        Object o = redisUtil.get("captcha:" + dto.getVerKey());
        AssertUtils.isTrue(null == o, "验证码已过期", Code.FAIL);
        String redisCode = String.valueOf(o);
        if (!dto.getCode().toLowerCase().equals(redisCode)) {
            AssertUtils.isTrue(true, "验证码错误", Code.FAIL);
        }
        SysUser user = this.getUserByUsername(dto.getUsername());
        AssertUtils.isTrue(null == user, "该用户不存在，请检查用户名是否错误", Code.FAIL);
        AssertUtils.isTrue(false == user.getState(), "非常抱歉，该账号已被冻结，如有疑问，请联系管理员", Code.FAIL);
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        boolean matches = encode.matches(dto.getPassword(), user.getPassword());
        AssertUtils.isTrue(!matches, "密码错误，请重新输入", Code.FAIL);

        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId());
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapper);
        List<Long> roleIdList = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<String> roleNameList = null;
        if (roleIdList.size() > 0) {
            List<SysRole> sysRoles = sysRoleMapper.selectBatchIds(roleIdList);
            roleNameList = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        }
        JSONObject obj = new JSONObject();
        obj.put("role", roleNameList);
        obj.put("username", user.getUsername());
        boolean hasKey = redisUtil.hasKey(user.getUsername());
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", user.getId());
        if (!hasKey) {
            String jwt = JwtUtils.createToken(user.getId(), obj.toString());
            redisUtil.set(jwt, user, CACHE_TIME);
            redisUtil.set(user.getUsername(), jwt, CACHE_TIME);
            jsonObj.put("token", jwt);
            return new NormalResponse<JSONObject>(Code.SUCCESS, "登录成功").setData(jsonObj);
        }
        jsonObj.put("token", redisUtil.get(user.getUsername()));
        return new NormalResponse<JSONObject>(Code.SUCCESS, "登录成功").setData(jsonObj);
    }

    @Override
    public NormalResponse findUser(String username) {
        SysUser sysUser = this.getUserByUsername(username);
        if (null == sysUser) {
            return new NormalResponse(Code.ARGUMENT_ERROR);
        }
        return new NormalResponse(Code.SUCCESS).setData(sysUser.getUsername());
    }

    @Override
    public NormalResponse addUser(SysUserDTO dto) {
        SysUser sysUser;
        if(null == dto.getId()){
            sysUser = new SysUser();
            sysUser.setUsername(dto.getUsername());
            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
            sysUser.setPassword(encode.encode(dto.getPassword()));
            sysUser.setSex(dto.getSex());
            if (StringUtils.isNotBlank(dto.getPhone())) {
                sysUser.setPhone(dto.getPhone());
            }
            sysUser.setNickname(dto.getNickname());
            int num = sysUserMapper.insert(sysUser);
            AssertUtils.isTrue(num < 1, "添加失败", Code.FAIL);

            Long[] roleIds = dto.getRoleIds();
            for (int i = 0; i < roleIds.length; i++) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(roleIds[i]);
                int count = sysUserRoleMapper.insert(sysUserRole);
                AssertUtils.isTrue(count < 1, "添加失败", Code.FAIL);
            }
            return new NormalResponse(Code.SUCCESS, "添加成功");
        }
        sysUser = sysUserMapper.selectById(dto.getId());
        List<SysUserRole> userRoleList = sysUserRoleMapper.findUserRoleList(dto.getId());
        List<Long> dbRoleIds = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        sysUser.setNickname(dto.getNickname());
        sysUser.setUsername(dto.getUsername());
        sysUser.setSex(dto.getSex());
        if (StringUtils.isNotBlank(dto.getPhone())) {
            sysUser.setPhone(dto.getPhone());
        }
        Long[] roleIds = dto.getRoleIds();
        if (dbRoleIds.size() < roleIds.length) {
            Long[] substract = ArrayUtils.substract(roleIds, dbRoleIds.toArray(new Long[0]));
            for (int i = 0; i < substract.length; i++) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(dto.getId());
                sysUserRole.setRoleId(substract[i]);
                int count = sysUserRoleMapper.insert(sysUserRole);
                AssertUtils.isTrue(count < 1, "添加失败", Code.FAIL);
            }
        } else if (dbRoleIds.size() > roleIds.length) {
            Long[] substract = ArrayUtils.substract(dbRoleIds.toArray(new Long[0]), roleIds);
            for (int i = 0; i < substract.length; i++) {
                QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", dto.getId()).eq("role_id", substract[i]);
                int count = sysUserRoleMapper.delete(wrapper);
                AssertUtils.isTrue(count < 1, "删除失败", Code.FAIL);
            }
        }
        int num = sysUserMapper.updateById(sysUser);
        AssertUtils.isTrue(num < 1, "修改失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "修改成功");
    }

    @Override
    public NormalResponse deleteBatchUser(Long[] id) {
        int num = sysUserMapper.deleteBatchIds(Arrays.asList(id));
        AssertUtils.isTrue(num != id.length, "删除失败", Code.FAIL);
        for (int i = 0; i < id.length; i++) {
            List<SysUserRole> userRoleList = sysUserRoleMapper.findUserRoleList(id[i]);
            List<Long> collect = userRoleList.stream().map(SysUserRole::getId).collect(Collectors.toList());
            int count = sysUserRoleMapper.deleteBatchIds(collect);
            AssertUtils.isTrue(count != collect.size(), "删除失败", Code.FAIL);
        }
        return new NormalResponse(Code.SUCCESS, "删除成功");
    }

    @Override
    public NormalResponse deleteUser(Long id) {
        int num = sysUserMapper.deleteById(id);
        AssertUtils.isTrue(num < 1, "删除失败", Code.FAIL);
        List<SysUserRole> userRoleList = sysUserRoleMapper.findUserRoleList(id);
        List<Long> collect = userRoleList.stream().map(SysUserRole::getId).collect(Collectors.toList());
        int i = sysUserRoleMapper.deleteBatchIds(collect);
        AssertUtils.isTrue(i != collect.size(), "删除失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "删除成功");
    }

    @Override
    public NormalResponse findUserList(FindUserListDTO dto) {
        Page<SysUser> page = new Page<>(dto.getPage(), dto.getLimit());
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id", "username", "nickname", "sex", "phone", "state", "gmt_create", "gmt_modified");
        if (StringUtils.isNotBlank(dto.getUsername())) {
            wrapper.like("username", dto.getUsername());
        }
        if (StringUtils.isNotBlank(dto.getNickname())) {
            wrapper.like("nickname", dto.getNickname());
        }
        if (null != dto.getSex()) {
            wrapper.eq("sex", dto.getSex());
        }
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(page, wrapper);
        List<SysUser> sysUserList = sysUserPage.getRecords();

        QueryWrapper<SysDict> dictWrapper = new QueryWrapper<>();
        dictWrapper.select("data_code", "data_value").eq("data_type", "sex");
        List<SysDict> sexList = sysDictMapper.selectList(dictWrapper);

        QueryWrapper<SysRole> roleWrapper = new QueryWrapper<>();
        roleWrapper.select("id", "role_name");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(roleWrapper);

        List<SysUser> collect = sysUserList.stream().filter(sysUser -> {
            sexList.forEach(sex -> {
                Integer code = Integer.valueOf(sex.getDataCode());
                if (sysUser.getSex().equals(code)) {
                    sysUser.setSexName(sex.getDataValue());
                }
            });
            List<SysUserRole> userRoleList = sysUserRoleMapper.findUserRoleList(sysUser.getId());
            SysRole[] roles = new SysRole[userRoleList.size()];
            sysRoleList.forEach(role -> {
                for (int i = 0; i < userRoleList.size(); i++) {
                    if (role.getId().equals(userRoleList.get(i).getRoleId())) {
                        roles[i] = role;
                    }
                }
            });
            sysUser.setRoles(roles);
            return true;
        }).collect(Collectors.toList());
        return new NormalResponse<List<SysUser>>(Code.SUCCESS, "查询成功").setData(collect);
    }

    @Override
    public NormalResponse resetPassword(Integer id) {
        SysUser sysUser = this.getUserById(id);
        AssertUtils.isTrue(null == sysUser, "该用户不存在", Code.FAIL);
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        sysUser.setPassword(encode.encode("123456"));
        int num = sysUserMapper.updateById(sysUser);
        AssertUtils.isTrue(num < 1, "重置密码失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "重置成功");
    }

    @Override
    public NormalResponse updateState(Integer id, Integer state) {
        AssertUtils.isTrue(null == id, "用户ID不能为空", Code.ARGUMENT_ERROR);
        AssertUtils.isTrue(null == state, "状态不能为空", Code.ARGUMENT_ERROR);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id", "state", "gmt_modified").eq("id", id);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        sysUser.setState(state == 1);
        int i = sysUserMapper.updateById(sysUser);
        AssertUtils.isTrue(i < 1, "修改失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "修改成功");
    }

    private SysUser getUserByUsername(String username) {
        AssertUtils.isTrue(StringUtils.isBlank(username), "用户名不能为空", Code.ARGUMENT_ERROR);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    private SysUser getUserById(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        return sysUser;
    }
}

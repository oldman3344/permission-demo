package com.oldman.permission.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import com.oldman.permission.common.jwt.JwtUtils;
import com.oldman.permission.common.redis.RedisUtil;
import com.oldman.permission.common.util.AssertUtils;
import com.oldman.permission.dto.SysUserDTO;
import com.oldman.permission.dto.LoginDTO;
import com.oldman.permission.mapper.SysRoleMapper;
import com.oldman.permission.mapper.SysUserRoleMapper;
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
    private RedisUtil redisUtil;
    @Value("${cache.time}")
    private Integer CACHE_TIME;

    @Override
    public NormalResponse login(LoginDTO dto) {
        Object o = redisUtil.get("captcha:" + dto.getVerKey());
        AssertUtils.isTrue(null==o,"验证码已过期",Code.FAIL);
        String redisCode = String.valueOf(o);
        if (!dto.getCode().toLowerCase().equals(redisCode)){
            AssertUtils.isTrue(true,"验证码错误",Code.FAIL);
        }
        SysUser user = this.getUserByUsername(dto.getUsername());
        AssertUtils.isTrue(null == user, "该用户不存在，请检查用户名是否错误", Code.FAIL);
        AssertUtils.isTrue(true == user.getFreeze(), "非常抱歉，该账号已被冻结，如有疑问，请联系管理员", Code.FAIL);
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
            roleNameList = sysRoles.stream().map(SysRole::getName).collect(Collectors.toList());
        }
        JSONObject obj = new JSONObject();
        obj.put("role", roleNameList);
        obj.put("username", user.getUsername());
        boolean hasKey = redisUtil.hasKey(user.getUsername());
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id",user.getId());
        if (!hasKey) {
            String jwt = JwtUtils.createToken(user.getId(), obj.toString());
            redisUtil.set(jwt, user, CACHE_TIME);
            redisUtil.set(user.getUsername(), jwt, CACHE_TIME);
            jsonObj.put("token",jwt);
            return new NormalResponse<JSONObject>(Code.SUCCESS, "登录成功").setData(jsonObj);
        }
        jsonObj.put("token",redisUtil.get(user.getUsername()));
        return new NormalResponse<JSONObject>(Code.SUCCESS, "登录成功").setData(jsonObj);
    }

    @Override
    public NormalResponse addUser(SysUserDTO dto) {
        SysUser user = this.getUserByUsername(dto.getUsername());
        AssertUtils.isTrue(null != user, "添加失败，该用户已存在", Code.FAIL);
        SysUser sysUser = new SysUser();
        sysUser.setUsername(dto.getUsername());
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        sysUser.setPassword(encode.encode(dto.getPassword()));
        int num = sysUserMapper.insert(sysUser);
        AssertUtils.isTrue(num < 1, "添加失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "添加成功");
    }

    @Override
    public NormalResponse updateUser(SysUserDTO dto) {
        SysUser sysUser = sysUserMapper.selectById(dto.getId());
        AssertUtils.isTrue(null == sysUser, "该用户不存在", Code.FAIL);
        sysUser.setFreeze(dto.getFreeze());
        int num = sysUserMapper.updateById(sysUser);
        AssertUtils.isTrue(num < 1, "修改失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "修改成功");
    }

    @Override
    public NormalResponse deleteUser(SysUserDTO dto) {
        AssertUtils.isTrue(dto.getIds().size() < 1, "请选择删除的用户", Code.FAIL);
        int num = sysUserMapper.deleteBatchIds(dto.getIds());
        AssertUtils.isTrue(num != dto.getIds().size(), "删除失败", Code.FAIL);
        return new NormalResponse(Code.SUCCESS, "删除成功");
    }

    @Override
    public NormalResponse findUserList(SysUserDTO dto, String username) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(page, StringUtils.isNotBlank(username) ? new QueryWrapper<SysUser>().like("username", username) : null);
        List<SysUser> sysUserList = sysUserPage.getRecords();
        return new NormalResponse<List<SysUser>>(Code.SUCCESS, "查询成功").setData(sysUserList);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        AssertUtils.isTrue(StringUtils.isBlank(username), "用户名不能为空", Code.ARGUMENT_ERROR);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
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
    public NormalResponse getInfo(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        JSONArray roleArray = new JSONArray();
        JSONArray authorityArray = new JSONArray();
        authorityArray.add("user:add");
        authorityArray.add("role:add");
        roleArray.add("admin");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("nickname","管理员");
        jsonObj.put("authorities",authorityArray);
        jsonObj.put("roles",roleArray);
        return new NormalResponse<JSONObject>(Code.SUCCESS).setData(jsonObj);
    }

    private SysUser getUserById(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        return sysUser;
    }
}

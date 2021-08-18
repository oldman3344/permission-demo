package com.oldman.permission.common.util;

import com.oldman.permission.common.annotation.BindEntity;
import com.oldman.permission.common.annotation.BindField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * VO转换器
 * @author oldman
 * @date 2021/8/18 15:59
 *
 * ValueObjectTransfer<SysUserVO,SysUser> transfer = new ValueObjectTransfer<>();
 * List<SysUserVO> sysUserVOList = transfer.cast(sysUserList, SysUserVO.class);
 */
public class ValueObjectTransfer<VO,PO> {

    /**
     * 转换PO列表
     *
     * @param poList 需要转换的PO列表
     * @param voClass 需要转换后的VO类
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public List<VO> cast(List<PO> poList, Class<VO> voClass){
        List<VO> voList = new ArrayList<VO>();
        try {
            for (PO po : poList) {
                VO vo = this.cast(po, voClass);
                voList.add(vo);
            }
        }catch (Exception ignored){
        }
        return voList;
    }

    /**
     * PO->VO
     * @param po 需要转换的PO
     * @param voClass 需要转换后的VO类
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public VO cast(PO po, Class<VO> voClass){
        // 判断需要转换的PO是否跟VO所绑定的PO一致
        if (!voClass.isAnnotationPresent(BindEntity.class)) {
            //需要转换的PO与VO所绑定的PO不一致
            return null;
        }
        VO vo = null;
        try {
            // 反射获取VO的实例
            vo = (VO) ClassLoader.getSystemClassLoader().loadClass(voClass.getName()).newInstance();
        }catch (Exception e){
        }
        // 遍历VO所有声明的属性
        for (Field field : voClass.getDeclaredFields()) {
            Class type = field.getType();
            String name = field.getName();
            // 判断是否包含声明为BindField注解的属性
            if (field.isAnnotationPresent(BindField.class)) {
                BindField bindFieldName = field.getAnnotation(BindField.class);
                // 反射调用绑定的PO属性的get方法进行取值
                Object value = invokeGet(po, bindFieldName.value());
                // 反射调用执行该VO属性的set方法设置值
                //TODO 需要完善各种属性类型的转换
                if(type.isInstance(value)) {
                    invokeSet(vo, name, value);
                }
            }
        }
        return vo;
    }

    /**
     * 获取get方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取set方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行set方法
     *
     * @param o
     * @param fieldName
     * @param value
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行get方法
     *
     * @param o
     * @param fieldName
     * @return
     */
    public static Object invokeGet(Object o, String fieldName) {
        Method method = getGetMethod(o.getClass(), fieldName);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

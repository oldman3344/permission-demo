package com.oldman.permission.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author oldman
 * @since 2021-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由，以/开头
     */
    private String path;

    /**
     * 父级ID（默认：0）
     */
    private Long parentId;

    /**
     * 为true只注册路由不显示在左侧菜单（默认：1true）
     */
    @TableField(value = "is_hide")
    private Boolean hide;

    /**
     * 是否隐藏全局页脚（默认：1是）
     */
    @TableField(value = "is_is_hideFooter")
    private Boolean hideFooter;

    /**
     * 是否隐藏侧边栏（默认：0否）
     */
    @TableField(value = "is_hideSidebar")
    private Boolean hideSidebar;

    /**
     * 是否可以在多页签中关闭（默认：1是）
     */
    @TableField(value = "is_closable")
    private Boolean closable;

    /**
     * 同路由不同参数页签是否只显示一个（默认：1是）
     */
    @TableField(value = "is_tabUnique")
    private Boolean tabUnique;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long gmtModified;
}

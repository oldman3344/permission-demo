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
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 路由
     */
    private String url;

    /**
     * 状态（0未停用 1已停用）
     */
    @TableField(value = "is_state",fill = FieldFill.INSERT)
    private Integer state;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 类型（0菜单 1按钮）
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;


}

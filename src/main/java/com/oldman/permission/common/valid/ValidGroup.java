package com.oldman.permission.common.valid;

import javax.validation.groups.Default;

/**
 * 分组校验
 * @author oldman
 * @date 2021/8/14 18:03
 */
public interface ValidGroup extends Default {
    interface Crud extends ValidGroup{
        interface Create extends Crud{
        }
        interface Delete extends Crud{
        }
        interface Update extends Crud{
        }

        interface Query extends Crud{
        }
    }
}

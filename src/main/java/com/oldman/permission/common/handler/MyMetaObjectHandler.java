package com.oldman.permission.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * MP自动填充处理器
 * @author oldman
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtCreate", () -> LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Long.class);
        this.strictUpdateFill(metaObject, "gmtModified", () -> LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtModified", () -> LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(), Long.class);
    }
}

package com.oldman.permission.common.generation;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author oldman
 * @date 2021/8/14 11:41
 */
public class CodeAutoGenerator {
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        //配置策略
        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //String projectPath = scanner("模块名");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("oldman");
        gc.setOpen(false);
        //是否覆盖原文件
        gc.setFileOverride(false);
        //去除service的I前缀
        gc.setServiceName("%sService");
        //ID生成策略 雪花算法
        //gc.setIdType(IdType.ASSIGN_ID);
        //gc.setDateType(DateType.ONLY_DATE);
        //gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/permission_demo?characterEncoding=UTF-8&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);


        //3、包的配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名标识"));
        pc.setParent("com.oldman")
                .setEntity("pojo")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();

        //设置要映射的表名，多个表名，数组形式
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));

        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //自动Lombok
        strategy.setEntityLombokModel(true);
        //自动逻辑删除
        //strategy.setLogicDeleteFieldName("deleted");
        //自动填充配置
        TableFill state = new TableFill("is_state", FieldFill.INSERT);
        TableFill createTime = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill updateTime = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(state);
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);
        //乐观锁配置
        //strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        //localhost:8080/hello_id_2
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);

        //执行
        mpg.execute();
    }
}

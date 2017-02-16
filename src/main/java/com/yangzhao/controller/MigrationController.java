package com.yangzhao.controller;

import com.yangzhao.bean.Property;
import com.yangzhao.database_operation.Operation;
import com.yangzhao.database_operation.OperationFactory;
import com.yangzhao.datasources.DataSourceContextHolder;
import com.yangzhao.datasources.DataSourceManager;
import com.yangzhao.service.MigrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yangzhao on 17/2/7.
 */
@Controller
public class MigrationController {

    @Resource(name = "operationFactory")
    private OperationFactory operationFactory;

    @Resource(name = "migrationServiceImpl")
    private MigrationService migrationServiceImpl;

    @RequestMapping("/test")
    public String test(ModelMap modelMap){
        return "index";
    }

    @RequestMapping("/create_datasource")
    @ResponseBody
    public Object createDataSource(HttpSession session, Property property, int flag, int databaseType){
        switch (databaseType){
            case 1:
                property.setDriver("com.mysql.jdbc.Driver");
                break;
            case 2:
                break;
            case 3:
                property.setDriver("org.postgresql.Driver");
                break;
            case 4:
                property.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
        }
        boolean result = migrationServiceImpl.createDataSource(session.getId(), property, flag);
        return result;
    }

    @RequestMapping("/tables")
    @ResponseBody
    public Object getTables(HttpSession session,int databaseType,int flag){
        Operation operation = operationFactory.getOperation(databaseType);
        DataSource dataSource = DataSourceManager.getInstance().getUserDataSource(session.getId(), flag);
        DataSourceContextHolder.setDataSource(dataSource);
        List<String> tables = operation.queryTables();
        Collections.sort(tables);
        return tables;
    }

    @RequestMapping("/table/fields")
    @ResponseBody
    public Object getTableSchema(HttpSession session,String tableName,int databaseType,int flag){
        DataSource dataSource = DataSourceManager.getInstance().getUserDataSource(session.getId(), flag);
        DataSourceContextHolder.setDataSource(dataSource);
        Operation operation = operationFactory.getOperation(databaseType);
        List<Map<String, Object>> mapList = operation.queryTableSchema(tableName);
        List<String> fields = mapList.stream().map(map -> map.get("Field").toString()).collect(Collectors.toList());
        Collections.sort(fields);
        return fields;
    }

    @RequestMapping("/data/migration")
    @ResponseBody
    public Object migration(HttpSession session,String[]fields,String[]targetFields,String tableName,String targetTableName){
        boolean b = migrationServiceImpl.dataMigration(session.getId(), fields, targetFields, tableName, targetTableName);
        return b;
    }

    @RequestMapping("/sql/execute")
    @ResponseBody
    public Object execute(HttpSession session,String sourceSql,String tegetSql){
        boolean execute = migrationServiceImpl.execute(session.getId(), sourceSql, tegetSql);
        return execute;
    }
}

package com.yangzhao.database_operation;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by yangzhao on 17/2/8.
 */
@Component
public class OperationFactory {

    @Resource(name = "mysqlOperation")
    private MysqlOperation mysqlOperation;

    @Resource(name = "postgresqlOperation")
    private PostgresqlOperation postgresqlOperation;

    @Resource(name = "sqlServerOperation")
    private SQLServerOperation sqlServerOperation;

    private Operation operation;

    public Operation getOperation(int type){

        switch (type){
            case 1:
                operation = mysqlOperation;
                break;
            case 2:
                break;
            case 3:
                operation = postgresqlOperation;
                break;
            case 4:
                operation = sqlServerOperation;
                break;
        }
        return operation;
    }
}

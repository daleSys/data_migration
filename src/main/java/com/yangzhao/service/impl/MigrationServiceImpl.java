package com.yangzhao.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.StringUtils;
import com.yangzhao.bean.Property;
import com.yangzhao.dao.BaseDao;
import com.yangzhao.datasources.DataSourceManager;
import com.yangzhao.service.MigrationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzhao on 17/2/7.
 */
@Service
public class MigrationServiceImpl implements MigrationService {

    @Resource(name = "baseDao")
    private BaseDao baseDao;

    @Override
    public boolean createDataSource(String sessionId, Property property,int flag) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(property.getUserName());
        dataSource.setPassword(property.getPassWord());
        dataSource.setUrl(property.getUrl());
        dataSource.setDriverClassName(property.getDriver());
        if (flag==1){
            DataSourceManager.getInstance().setUserDataSources(sessionId,"read",dataSource);
        }else if (flag==2){
            DataSourceManager.getInstance().setUserDataSources(sessionId,"write",dataSource);
        }
        DruidPooledConnection connection = null;
        try {
            connection = dataSource.getConnection();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if (connection!=null){
            return true;
        }
        return false;
    }

    @Override
    public boolean dataMigration(String sessionId,String[] fields, String[] targetFields, String tableName, String targetTableName) {
        Map userDataSources = DataSourceManager.getInstance().getUserDataSources(sessionId);
        DataSource readDataSource = (DataSource) userDataSources.get("read");
        DataSource writeDataSource = (DataSource) userDataSources.get("write");
        StringBuilder readFields = new StringBuilder();
        for (String field:fields) {
            if (!StringUtils.isEmpty(field)) {
                readFields.append(field + ",");
            }
        }
        String rf = readFields.toString();
        rf=rf.substring(0,rf.length()-1);
        String readSql = "select "+rf+" from "+tableName;
        baseDao.getJdbcTemplate().setDataSource(readDataSource);
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(readSql);
        StringBuilder writeFields = new StringBuilder();
        for (String targetField:targetFields) {
            if (!StringUtils.isEmpty(targetField)) {
                writeFields.append(targetField + ",");
            }
        }
        String wf = writeFields.toString();
        wf=wf.substring(0,wf.length()-1);
        StringBuilder wirteSql = new StringBuilder();
        List values = new ArrayList();
        wirteSql.append("insert into "+targetTableName+" ("+wf+") values");
        for (Map data:mapList) {
            StringBuilder p = new StringBuilder();
            p.append("(");
            data.forEach((key,value)->{
                p.append("?,");
                values.add(value);
            });
            String s = p.toString();
            s=s.substring(0,s.length()-1);
            s+="),";
            wirteSql.append(s);
        }
        baseDao.getJdbcTemplate().setDataSource(writeDataSource);
        String ws = wirteSql.toString();
        ws=ws.substring(0,ws.length()-1);

        int insert = baseDao.getJdbcTemplate().update(ws,values.toArray());;
        if (insert>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean execute(String sessionId, String sourceSql, String targetSql) {
        Map userDataSources = DataSourceManager.getInstance().getUserDataSources(sessionId);
        DataSource dataSource = (DataSource) userDataSources.get("read");
        baseDao.getJdbcTemplate().setDataSource(dataSource);
        List<Map<String, Object>> mapList = baseDao.getJdbcTemplate().queryForList(sourceSql);
        StringBuilder values = new StringBuilder();
        List params = new ArrayList();
        mapList.stream().forEach(map->{
            StringBuilder value = new StringBuilder();
            value.append("(");
            map.forEach((k,v)->{
                value.append("?,");
                params.add(v);
            });

            String v = value.substring(0, value.length() - 1);
            v+="),";
            values.append(v);
        });
        String s = values.toString();
        s = s.substring(0,s.length()-1);
        targetSql+=s;
        DataSource writeDataSource = (DataSource) userDataSources.get("write");
        baseDao.getJdbcTemplate().setDataSource(writeDataSource);
        int update = baseDao.getJdbcTemplate().update(targetSql, params.toArray());
        if (update>0){
            return true;
        }
        return false;
    }
}
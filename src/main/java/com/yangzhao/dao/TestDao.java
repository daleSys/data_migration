package com.yangzhao.dao;

import com.yangzhao.datasources.DataSourceManager;
import com.yangzhao.datasources.Read;
import com.yangzhao.datasources.Write;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzhao on 17/2/7.
 */
@Repository
public class TestDao extends BaseDao {

    public void test(String sessionId){
        Map userDataSources = DataSourceManager.getInstance().getUserDataSources(sessionId);
        DataSource readDataSource = (DataSource) userDataSources.get("read");
        setDataSource(readDataSource);
        String sql = "SELECT xm.xiangmu_name,nr.neirong_mc,nr.neirong_jc,nr.neirong_zd,nr.neirong_order,nr.neirong_dw FROM lis_xiangmu xm LEFT JOIN lis_neirong nr on xm.\"id\"=nr.xm_id";
        List<Map<String, Object>> mapList = getJdbcTemplate().queryForList(sql);
        DataSource writeDataSource = (DataSource) userDataSources.get("write");
        setDataSource(writeDataSource);
        mapList.stream().forEach(map->{
            Object xiangmu_name = map.get("xiangmu_name");
            Object neirong_mc = map.get("neirong_mc");
            Object neirong_jc = map.get("neirong_jc");
            Object neirong_order = map.get("neirong_order");
            Object neirong_dw = map.get("neirong_dw");
            String idSql = "SELECT id FROM huayan_option WHERE `name`='"+ xiangmu_name.toString()+"'";
            Integer integer = getJdbcTemplate().queryForObject(idSql, int.class);
            int parentId = integer.intValue();
            String wirteSql = "INSERT INTO huayan_option (name,abbreviation,sort,dw,parent_id) value (?,?,?,?,?) ";
            Object[]params = new Object[]{neirong_mc,neirong_jc,neirong_order,neirong_dw,parentId};
            int update = getJdbcTemplate().update(wirteSql, params);
            System.out.println(update);
        });

    }

}

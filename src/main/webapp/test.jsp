<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.yangzhao.database_operation.Operation" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yangzhao.database_operation.OperationFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="com.yangzhao.datasources.DataSourceManager" %>
<%@ page import="com.yangzhao.datasources.DataSourceContextHolder" %>
<%--
  Created by IntelliJ IDEA.
  User: yangzhao
  Date: 17/2/8
  Time: 上午10:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="//cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<%
    String tableName = "xt_huayan_zlbzwqt";
    DataSource dataSource = DataSourceManager.getInstance().getUserDataSource(session.getId(), 2);
    DataSourceContextHolder.setDataSource(dataSource);
    ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
    OperationFactory operationFactory = (OperationFactory) applicationContext.getBean("operationFactory");
    Operation operation = operationFactory.getOperation(1);
    List<Map<String, Object>> mapList = operation.queryTableSchema(tableName);
    List<String> fields = new ArrayList<>();
    for (Map map:mapList) {
        String field = map.get("Field").toString();
        fields.add(field);
    }
    StringBuilder params = new StringBuilder();
    String insert ="insert into "+tableName +" (";
    for (String field:fields) {
        if (field.equals("id")){
            continue;
        }
        params.append(field+",");
    }
    String p = params.toString();
    p=p.substring(0,p.length()-1);
    insert+=p;
    insert+=") values";
    out.println(insert);
    %>
    <hr>
    <%
    out.println("select "+p+" from "+tableName);

%>
</body>
</html>

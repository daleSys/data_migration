<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.yangzhao.database_operation.MysqlOperation" %>
<%@ page import="com.yangzhao.dao.TestDao" %><%--
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
    ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
    TestDao bean = context.getBean(TestDao.class);
    bean.test(request.getSession().getId());
%>
</body>
</html>

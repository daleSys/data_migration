# data_migration

####不同数据库之间数据同步

目前已实现mysql、postgresql、sqlserver三个数据库之间数据同步。

####两种方式实现数据迁移：

####①页面交互
   
  数据库连接成功后，通过点击按钮查询所有表显示当前数据库所有表名然后点击表名显示该表的所有字段，只需要勾选需要操作的字段。
  
####   ★★★★★这种方式只支持源数据库select，目标数据库insert操作
*源数据库、目标数据库操作是一样的*

####②SQL输入
这种方式比较简单就是，在页面中输入即将在两边数据库执行的sql语句然后点击执行按钮即可


# 简介
一款轻量级的个人博客系统，基于SpringBoot 2.13，融合了hutool，Ckeditor，PageHelper,JustAuth等工具，可以让写blog变得和写word文档一样舒服！
# 版本信息
**V1.2**  2020年2月28日 修复后台管理首页数据统计日期不正常的Bug
# 引导

下面是ZJGBlog的快速引导，能够帮助你跑起该项目。

# 运行环境
JDK 1.8.121 x64 

Mysql 5.7.27 x64

Intellij Idea 2019.2 x64

注：不同的Mysql版本可能会产生语法差异，需要自行百度。

# 数据库配置

运行环境安装完成后，导入blog.sql文件。导入成功后，打开下载好的maven项目，进行相关的数据库配置。如果有数据表属性改动，可以直接运行generator包内的逆向工程函数，重新生成mapper，entity以及mybatis配置文件，注意SysViewMapper内有自定义统计语句，小心覆盖。
# 项目配置
## application.properties
必配参数：
-----数据库------
spring.datasource.username = xxxx  
spring.datasource.password = xxxx
-----图片存储路径--------
images.upload.path=
----------邮件参数设置  ----------
blog.mail.host=  
blog.mail.pass=  
blog.mail.from=
--------后台管理系统登录密码------  
admin.username=  
admin.password=

选配参数：
---------第三方登录-------
justauth.type.QQ.client-id=  
justauth.type.QQ.client-secret=  
justauth.type.QQ.redirect-uri=  
justauth.type.github.client-id=  
justauth.type.github.client-secret=  
justauth.type.github.redirect-uri=
----------ssl配置---------  
ssl证书路径  
server.ssl.key-store=  
ssl证书密码  
server.ssl.key-store-password=  
证书类型  
server.ssl.key-store-type=
## BlogApplication.java
代码注释为https访问用，如果配置了ssl证书，需要将注释去掉
## Swagger2.java
代码注释为调试api接口用，如果需要调试接口，请取消注释代码，并在浏览器内输入以下url:[https://localhost:8080/swagger-ui.html](https://localhost/swagger-ui.html)
（建议先打开，然后访问以方便查看网站接口情况）
# 其他
SSL证书配置教程以及邮箱配置在我的个人主页有教程，可直接查看 https://www.zhoujianguo.ltd
如果本项目对你有所帮助，请star，你的支持是本项目日后优化和进步的最大动力！
有任何问题或者建议，你可以加入QQ群：828944177进行交流。
请勿用于商业用途，仅供参考学习!
本文更新于：2020年2月28日18:14:16

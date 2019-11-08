本项目是一个分布式的App服务端快速开发框架,包含了基本的权限认证、日志处理、接口防刷、系统监控等基本功能。
此框架围绕分布式服务系统构建，能够快速扩容，迎合微服务化，提供App服务端常用必备功能。  
** 升级到spring boot 2.1 添加缓存功能
### 技术栈：
> 1. Spring Boot / Spring MVC / Spring Data Jpa
> 2. Gradle 5.0
> 3. Java8
> 4. Logback
> 5. Lombok
> 6. jpush
> 7. ali oss
> 8. spring cloud


### 功能列表：
> 1. 认证方式： Basic、 Cookie、Header、内外网
> 2. 统一错误处理、统一Json格式模板
> 3. 接口请求日志统一处理
> 4. 接口频次拦截
> 5. 支持多数据源、主从分离
> 6. 多Profile支持，Gradle、Spring、应用程序Profile整合
> 7. 完善的系统监控
> 8. 热部署
> 9. 自动生成接口文档
> 10. 读取配置中心配置，自动刷新
> 11. 以ip地址注册到注册中心提供服务


#### 环境配置
区分有三种环境dev、test、prod，不同环境会加载不同的配置文件
> 1. Gradle环境配置: gradle.properties里设置profile
> 2. Spring环境变量: application.yaml或application.properties里配置spring.profiles.active
> 3. 应用内获取环境变量: spring注入: @Autowired Environment env 或手动解析spring配置文件（不依赖Spring）

#### 数据源配置
> 1. 如果安装了docker，直接执行 deploy/bin/init_redis.sh脚本
> 2. 手动安装:
>     redis: ip:127.0.0.1 port:6379 password:无

#### 可执行jar包
运行 gradle clean jar assemble 会自动打可执行jar包，运行：
> 1. java -jar deploy/build/libs/deploy-${version}.jar
> 2. ./deploy/build/libs/deploy-${version}.jar 如需配置JVM等参数请修复deploy/config/deploy-${version}.conf并拷贝到可执行jar包相同目录，并修改${version}

#### 发布jar/war包到私有仓库
> 1. 修改build.gradle里uploadArchives的私有仓库地址、用户名、密码
> 2. 执行 ./gradlew uploadArchives 命令

#### 运行项目方式
> 1. 执行: gradle run
> 2. 执行运行: Application.java
> 3. 执行: ./gradlew run，此方式不用安装gradle

#### 监控
> * 健康检查： http://localhost:7002/health
> * 次数监控： http://localhost:7002/metrics
> * APP信息： http://localhost:7002/info
> * dump信息： http://localhost:7002/dump
> * 环境信息： http://localhost:7002/env
> * 性能监控： http://localhost:8080/javasimon
> * 数据库监控： http://localhost:8080/druid
> * Tomcat监控： http://localhost:7002/jolokia/read/Tomcat:type=Connector,port=8080

#### 接口文档
> * swagger: http://localhost:8080/swagger-ui.html

#### TODO
> * 完善注释
> * 完善文档
> * 添加单元测试、集成测试、压力测试
> * 添加 cat 监控

#### 安装 redis 6379 6380
>1. http://www.redis.io/ 找到想要安装的版本下载地址
>2. wget http://download.redis.io/releases/redis-5.0.3.tar.gz
>3. tar -zxf redis-5.0.3.tar.gz
>4. cd redis-5.0.3  /
>5. make
>6. 编译完成后，在Src目录下，有四个可执行文件redis-server、redis-benchmark、redis-cli和redis.conf。然后拷贝到一个目录下。
>7. mkdir /usr/local/redis
>8. cp redis-server /usr/local/redis/
>9. cp redis-benchmark /usr/local/redis/
>10. cp redis-cli /usr/local/redis/
>11. cp redis.conf /usr/local/redis/
>12. cd /usr/local/redis
>13. ./redis-server redis.conf 启动redis
>14. 把daemonize设置为yes 可以后台启动
>15. \# requirepass foobared  \#号去掉 密码设置为自己的
>16. maxmemory and maxmemory_policy  
>17. maxmemory 1G 
>18. maxmemory-policy volatile-ttl
>1. `redis-server --service-install redis.windows.conf --service-name redis6379 --loglevel verbose`

#### 安装mysql
>1. https://dev.mysql.com/downloads/repo/yum/ 找到repo下载链接下载
>2. 安装 sudo rpm -Uvh mysql80-community-release-el6-n.noarch.rpm
>3. 查看 yum repolist all | grep mysql
>4. 禁用其他版本 sudo yum-config-manager --disable mysql80-community
>5. 开启5.7版本 sudo yum-config-manager --enable mysql57-community
>6. 查看当前开启版本 yum repolist enabled | grep mysql
>7. 安装mysql sudo yum install mysql-community-server
>8. 启动mysql sudo service mysqld start   或者 sudo systemctl start mysqld.service
>9. 查看密码 sudo grep 'temporary password' /var/log/mysqld.log
>10. 进入MySQL mysql -uroot -p
>11. 修改密码 ALTER USER 'root'@'localhost' IDENTIFIED BY 'MyNewPass4!';  

#### 升级spring boot 2.1
> 1. 升级依赖
> 2. application.yml中添加 main: allow-bean-definition-overriding: true  
> 3. 数据库连接修改`useUnicode=true&useSSL=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8`
> 4. 数据库驱动修改`com.mysql.cj.jdbc.Driver`
> 5. durid filters 去掉 log4j
> 6. `extends WebMvcConfigurerAdapter` 修改为 `implements WebMvcConfigurer`
> 7. `new PageRequest` 修改为 `PageRequest.of`
> 8. `dao.save(list)` 修改为 `dao.saveAll(list)`
> 9. `dao.findOne()` 修改为 `dao.findById()`

#### 使用spring cache redis
> 1. 添加 `@EnableCaching`
> 2. 编写 `RedisCacheConfig`
> 3. 编写 `FastJsonRedisSerializer.java`
> 4. 缓存对象实现 `Serializable` 接口 和 `@NoArgsConstructor`
> 5. 使用 `@Cacheable(cacheNames = "name", key = "#root.args[0]")` 对接口缓存
> 6. 和统计数据使用不同 redis 实例 （隔离影响）
> 7. `sync = true` 只有一个请求可以去请求数据库


#### bugs

#### todo

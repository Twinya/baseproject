配置文件统一配置，主要包含properties配置

#### 区分三种环境配置  
* dev  本地开发环境 配置目录： src/main/resources_dev  
* test 测试环境 配置目录： src/main/resources_test  
* prod 线上环境 配置目录： src/main/resources_prod  

src/main/resources 放置公共配置
#### 不同项目读取不同配置,从配置中心读取

#### springcloud配置中心+webhook实现配置自动刷新
* 引入依赖 spring-cloud-starter-bus-amqp,spring-cloud-config-server,spring-boot-starter-actuator

* 安装erlang
    > 1. 安装依赖环境 yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel
    > 2. 下载 wget http://erlang.org/download/otp_src_20.3.tar.gz 【可根据官网信息下载最新】
    > 3. 解压并安装 tar -zxvf otp_src_20.3.tar.gz ；make install 
    
* 安装rabbitmq
    > 1. 官网 http://www.rabbitmq.com/which-erlang.html
    > 2. wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.8/rabbitmq-server-generic-unix-3.7.8.tar.xz
         解压 tar -xvf rabbitmq-server-generic-unix-3.7.8.tar.xz
    > 3. 后台启动rabbitmq  ./rabbitmq-server -detached
    
* 添加远程账号登录
     > 1. 在/etc/rabbitmq/rabbitmq.config.example文件下添加 
        [
         {rabbit,[{tcp_listeners,[5672]},{loopback_users, ["jianlingmq"]}]}  jianlingmq就是用户名称
        ]
        
* 添加用户并授权，将路径切到rabbitmq的sbin目录下进行如下操作
    > 1. 开启后台管理页面 ./rabbitmq-plugins enable rabbitmq_management
    > 2. 添加用户 ./rabbitmqctl add_user 账号 密码
    > 3. 分配用户标签 ./rabbitmqctl set_user_tags jianlingmq administrator
    > 4. 设置权限 ./rabbitmqctl set_permissions -p "/" jianlingmq ".*" ".*" ".*"
    
* springboot添加配置信息
        spring.rabbitmq.host=小鱼内网IP
    	spring.rabbitmq.port=5672
    	spring.rabbitmq.username=jianling
    	spring.rabbitmq.password=jianling.123
  
* 需要给加载变量的类上面加载@RefreshScope

打包模块，包含Spring全局配置
#### 瘦身打包
只打包当前代码
`bootJar {
     classifier = 'boot'
     excludes = ["*.jar"]
 }`

## page view 和user view 系列数据表要建立channel 时间 唯一索引
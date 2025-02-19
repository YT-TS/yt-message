# yt-message

<p align="center">
	<img src="./doc/images/yt.jpeg" style="width: 200px; height: 200px">
</p>
<p align="center">
	<img src="https://img.shields.io/badge/JDK-17-red">
	<img src="https://img.shields.io/badge/SpringBoot-3-green" >
	<img src="https://img.shields.io/badge/MybatisPlus-3.5.10.1-red">
    <img src="https://img.shields.io/badge/本地缓存-Caffeine-orange">
    <img src="https://img.shields.io/badge/分布式缓存-Redis-red">
    <img src="https://img.shields.io/badge/定时任务-xxljob-green">
    <img src="https://img.shields.io/badge/注册中心-Nacos-blue">
    <img src="https://img.shields.io/badge/消息队列-RocketMQ-red">
    <img src="https://img.shields.io/badge/日志采集-Graylog-orange">
    <img src="https://img.shields.io/badge/实时计算引擎-Flink-red">
    <img src="https://img.shields.io/badge/微服务框架-Dubbo-yellow">
	<img src="https://img.shields.io/badge/前端页面-Vben Admin-blue">
    <img src="https://img.shields.io/badge/统计图表-echarts-green">
    <img src="https://img.shields.io/github/license/YT-TS/yt-message" alt="issue-close">
</p>

## 简介

yt-message是一个消息推送平台，提供各种类型消息推送功能，目前支持短信，邮箱，企业微信、飞书、钉钉机器人消息，并且支持自定义消息处理，未来将接入更多类型消息推送。

## 主要功能

- 消息平台：对接各种第三方平台，例如阿里，腾讯等等
- 消息模板：定义消息模板，包括消息内容，内容参数，接收账户，消息平台等等
- 账户组：预先定义消息接收的账户
- 预发送模板：提前设定消息模板和接收账户，一键发送
- 实时报表：使用flink实时处理记录发送的消息，并输出统计数据，使用echarts根据统计数据生成实时报表
- 消息记录：使用日志管理框架Graylog记录业务数据和应用日志，通过调用Graylog提供的Api检索日志，生成消息记录和消息轨迹，使用Graylog的的警告功能对error级别日志实现预警功能
- RPC接口：通过Dubbo提供RPC接口与其他项目集成
- 小功能：消息发送限流，消息重复处理等等
- ......

## 目录结构

```
YT-MESSAGE
├─yt-message-admin              管理端
├─yt-message-admin-ui           管理端前端
├─yt-message-api                向外提供RPC
├─yt-message-common             通用
├─yt-message-handler            消息处理端
├─yt-message-log                日志配置  
├─yt-message-mq                 消息队列篇配置
└─yt-message-stream-flink       流处理端
```

## 功能演示

### 1.创建三方平台

![platform](doc/images/platform.png)

### 2.创建消息模板

![](doc/images/template.png)

### 3.创建账户组

![accounts](doc/images/accounts.png)

### 4.创建预发送消息模板

![prepared_template](doc/images/prepared_template.png)

### 5.发送消息

**消息主要构成：（消息模板+消息内容参数+接收账户）或者 预发送消息模板**

#### 1.  消息模板发送

#### ![template_send](doc/images/template_send.png)2. 预发送消息模板发送

![](doc/images/prepared_template_send.png)



#### 3. RPC调用

例

引入api包

```
<dependency>
    <groupId>com.yt</groupId>
    <artifactId>yt-message-api</artifactId>
    <version>version</version>
</dependency>
```



```
	@DubboReference()
    private MessageService messageService;

    @Test
    void testSendMessage() {
         SendMessageRequest build = SendMessageRequest.sendEmailMessageRequestBuilder()
                    .templateId(333L)
                    .contentParams(new String[]{"1","22","aa"})
                    .subjectParams(new String[]{"3","bb","aa"})
                    .receiveAccount("702279350@qq.com")
                    .build();
            MessageSendRsp messageSendRsp = messageService.sendMessage(build);
       }
   }
```

### 6.统计图表

![statistics](doc/images/statistics.png)

### 7.消息记录

![message_record](doc/images/message_record.png)

### 8.error日志监控

![error_log](doc/images/error_log.png)

## 项目部署

待更新

## 后续更新

- [ ] 支持更多的消息类型 ，安卓通知栏、微信服务号、微信小程序、支付宝小程序等待

- [ ] 引入用户管理

- [ ] 延时消息

- [x] 运行时error日志页面

- [ ] ......

  

## 另外

求工作机会！！！
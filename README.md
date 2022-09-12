# ding-robot



## Features

ding-robot是一个基于[钉钉开放接口](https://open.dingtalk.com/document/group/custom-robot-access)开发的预警开发库，其特点是消息必达。

- 开箱即用，简单配置钉钉机器人[基本参数](https://open.dingtalk.com/document/group/customize-robot-security-settings)皆可使用
- 采用限流器避免消息发送过于频繁，机器人被禁言丢失重要告警信息（[关于调用频率](https://open.dingtalk.com/document/group/custom-robot-access)）
- 提供了基于内存的单机限流算法和基于Redis的分布式限流算法
- 高度容错，ding-robot框架的任何异常都不会影响业务接口
- 失败消息持久化，定时重试失败消息，确保消息正确预警
- 失败重试消息不抢占正常消息发送资源，确保正常消息优先级



## Prerequisite

ding-robot需要java8及以上版本

ding-robot基于spring-boot框架运行



## Building

> git clone  https://github.com/xpp011/ding-robot.git
>
> cd ding-robot
>
> mvn clean install -Dmaven.test.skip=true



## Quickstart

### 依赖

基于内存限流的单机版只需引用

```xml
<dependency>
    <groupId>cn.xpp011</groupId>
    <artifactId>ding-robot</artifactId>
    <version>${latest-version}</version>
</dependency>
```

基于Redis的分布式限流还需引用redis

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```



### 配置信息

配置redis信息

```yaml
spring:
  redis:
    host: 
    password: 
```

配置机器人地址

```yaml
warn:
  ding-robot:
    instance:
      test:
        webhook: https://oapi.dingtalk.com/robot/send?access_token=113ec2c6a446e9d6d8e723ce39aa6949cae5ed7aae66bbdc839e1389ea0d5562
        secret: SEC5014b30034fab4beef7595f066c2aedd610a1ba9ae998f4dd56492aa9b6ebc54
```



### 发送消息

```java
At at = new At.Builder()
        .setAtMobiles(Arrays.asList("phone"))
        .setAtAll(false)
        .builder();
Message text = new TextMessage.Builder()
        .setContent("测试消息")
        .setAt(at)
        .builder();
boolean test = dingRobotFactory.getDingRobot("test").send(text);
```



## Documentation

### 关于限流算法

对于需求`一分钟内最多发送20条消息，超过20条则会被禁言十分钟`，在常规的限流算法中，只有`滑动窗口，令牌桶，漏桶`可以满足这个需求，`固定窗口`的限流策略过于粗略，会出现临界时间的突发流量，故不采用`固定窗口`。

`滑动窗口`能够确保在时间窗口中不会出现>n的流量，且实现简单，是最合适预警服务的限流算法，所以ding-robot采用的就是基于`滑动窗口`的限流算法。

`令牌桶，漏桶`的限流算法在流量整形效果更加好，在时间颗粒度上处理的很平滑，能够确保流量曲线是很平滑的，但是它们并不适合预警服务，预警服务往往是需要应付突发流量的，不需要对流量整形效果有多好，只需满足在时间窗口内不会超过最大允许的限流值即可。



## Roadmap

| 功能                                   | 优先级 | 进度   |
| -------------------------------------- | ------ | ------ |
| 简化依赖包                             | p1     | 未开发 |
| 支持actionCard、feedCard的消息类型     | p1     | 未开发 |
| 完善测试报告                           | p1     | 未开发 |
| 支持更多的限流算法                     | p2     | 未开发 |
| 整合各种预警方式(企业微信、短信、邮件) | p1     | 未开发 |


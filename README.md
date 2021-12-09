<h1 align="center">Forge - 轻量级工作聚合锻造框架</h1>

# 快速开始

## 安装`Forge Spring Boot Starter`

- `latest` = `1.0.5-BETA`
- `latest`表示为推荐使用版本

- maven

```xml
<dependency>
    <groupId>com.bnyte</groupId>
    <artifactId>forge-spring-boot-starter</artifactId>
    <version>${latest}</version>
</dependency>
```

- gradle

```
implementation group: 'com.bnyte', name: 'forge-spring-boot-starter', version: ${latest}
```

## 使用

> 使用的前提是完成了`Forge Spring Boot Starter`的安装, `Forge Spring Boot Starter`完成里一些必要的前置准备，`这是先觉条件`

### 使用`API日志管理`

- 定义接口并在方法中声明`@APIHelper`

```
@RestController
@RequestMapping("/start")
public class StarterController {

    @RequestMapping("/get")
    @APIHelper
    public String get(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestBody User user) {
        return "成功拉";
    }
}
```
- 日志Demo

```
Request
	id: 35e0a678d3104421be48402dcb9bcfbb
	path: /start/get
	headers: {"content-length":"62","postman-token":"dd6bc5e0-eea0-4cc7-bc96-cf7139e9a0d4","host":"localhost:8080","content-type":"application/json","connection":"keep-alive","accept-encoding":"gzip, deflate, br","user-agent":"PostmanRuntime/7.28.4","accept":"*/*"}
	type: POST
	name: get
	body: ["bnyte","mima",{"username":"username..","password":"password.."}]
2021-12-09 12:29:47.376  INFO 3339 --- [nio-8080-exec-2] c.b.f.aop.actuator.APIHelperActuator     : 
Response
	id: 35e0a678d3104421be48402dcb9bcfbb
	status: 200
	body: 成功拉
	time: 26
```

- `@APIHelper`的强大自定义

|  参数名   | 备注  | 是否必填  |  默认值  |
|  ----  | ----  | ----  | ----  |
| value | 请求的API接口地址 | false | 当前接口的请求路径，如：`/start/get`，如果不为空则使用声明的值 | 
| name | 被执行的方法名 | false | 当前方法名，如果不为空则使用声明的值 | 
| enableRequest | 是否打开请求日志输出 | false | true | 
| enableResponse | 是否打开响应日志输出 | false | true | 
| output | 可选值包括：JSON、TO_STRING[需要人为手写toString()，否则输出为对象地址]、each[该方式需要人为提供gather()] | false | JSON |
| executeTime | 是否开启API执行时长，以毫秒为单位输出在`响应`日志中 | false | true |

<h1 align="center">Forge - 轻量级工作聚合锻造框架</h1>

# 快速开始

## 安装`Forge Spring Boot Starter`

- `latest` = `1.0.8.1`
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

> 使用的前提是完成了`Forge Spring Boot Starter`的安装, `Forge Spring Boot Starter`完成了一些必要的前置准备，`这是先觉条件`

### 使用`API日志管理`

- 定义接口并在方法中声明`@APIHelper`

```
@RestController
@RequestMapping("/start")
public class StarterController {

    // 获取forge构建请求日志对象
    @Autowired
    APIHelperActuator apiHelperActuator;

    @RequestMapping("/get/{id}")
    @APIHelper
    public Object get(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestBody User user,
                      @PathVariable("id") String id) {
        List<String> s = new ArrayList<>();
        s.add("1");
        s.add("1");
        s.add("1");
        s.add("1");
        String headers = apiHelperActuator.getHeaders();
        CurrentPathVariable pathVariable = apiHelperActuator.getPathVariable();
        CurrentQueryParam queryParam = apiHelperActuator.getQueryParam();
        CurrentBody body = apiHelperActuator.getBody();
        return s;
    }
}
```
- 日志Demo

```
Request
	id: c3ecf5f8956d483d9b74c695c76c1d97
	path: /start/get/路径id
	headers: {"content-length":"62","postman-token":"7bbc0c20-f0d6-43d5-8936-e567fe299660","host":"127.0.0.1:8080","content-type":"application/json","connection":"keep-alive","accept-encoding":"gzip, deflate, br","user-agent":"PostmanRuntime/7.28.4","accept":"*/*"}
	type: DELETE
	name: get
	pathVariable: {"id":"路径id"}
	query: {"password":"mima","username":"bnyte"}
	body: [{"username":"username..","password":"password.."}]
2021-12-13 16:01:56.277  INFO 6034 --- [nio-8080-exec-1] c.b.f.aop.actuator.APIHelperActuator     : 
Response
	id: c3ecf5f8956d483d9b74c695c76c1d97
	status: 200
	body: [1, 1, 1, 1]
	time: 1
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

# 响应式同意结果集

- 使用`com.bnyte.forge.http.reactive.web.R.ok()`可以直接响应你需要的结果集。

- 响应结果演示

```json
{
  "code": 0, 
  "message": "succeeded",
  "data": {}
}
```

TIPS:

> 如果调用`ok()`那么此时响应业务状态码为`0`，而您需要响应失败结果时只需要调用`error()`就可以了
>
> 通常情况下我们会将业务状态码定义为0为成功，-1为失败，而此时如果您需要其他的业务状态码则只需要继续使用对象来进行调用如`R.error().code(10001).message("request error")`;
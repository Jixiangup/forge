package com.bnyte.forge.aop.actuator;

import com.bnyte.forge.http.param.ForgeBody;
import com.bnyte.forge.http.param.ForgeHeader;
import com.bnyte.forge.http.param.ForgePathVariable;
import com.bnyte.forge.annotation.APIHelper;
import com.bnyte.forge.enums.HttpSchedule;
import com.bnyte.forge.enums.LogOutputType;
import com.bnyte.forge.http.param.ForgeQueryParam;
import com.bnyte.forge.util.JacksonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.*;

/**
 * APIHelper动态代理的执行器
 * @auther bnyte
 * @date 2021-12-04 03:08
 * @email bnytezz@gmail.com
 */
@Aspect
@Component
public class APIHelperActuator {

    private static final Logger log = LoggerFactory.getLogger(APIHelperActuator.class);

    /**
     * 本次请求的请求对象
     */
    private HttpServletRequest request;

    /**
     * 本次请求响应对象
     */
    private HttpServletResponse response;

    /**
     * 本次请求的响应结果，如果没有则为空
     */
    private Object result;

    /**
     * 本次请求代理的代理方法
     */
    private ProceedingJoinPoint point;

    /**
     * 本次请求发起时间
     */
    private long requestTime;

    /**
     * 本次请求的servlet请求对象
     */
    private ServletRequestAttributes attributes;

    /**
     * 本次请求目标执行的方法对象
     */
    private Method invokeMethod;

    /**
     * 本次请求执行的目标方法上方的@APIHelper注解标识
     */
    private APIHelper apiHelper;

    /**
     * 请求的请求头
     *  在下一个版本中将更新为map进行存储
     */
    private ForgeHeader headers = new ForgeHeader();

    /**
     * 日志输出方式：TO_STRING，JSON
     */
    private LogOutputType logOutputType;

    /**
     * 本次请求id
     */
    private String id;

    /**
     * 路径（URI）参数
     */
    private ForgePathVariable pathVariable = new ForgePathVariable();

    /**
     * 查询参数
     */
    private ForgeQueryParam queryParam = new ForgeQueryParam();

    /**
     * body参数
     */
    private ForgeBody body = new ForgeBody();

    /**
     * 被执行的目标方法中的参数值（该数组中是真正有值的数组 ）
     */
    private Object[] args;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.bnyte.forge.annotation.APIHelper)")
    public void pointcut(){}

    /**
     * 环绕通知，具体的接口日志输出点，改方式当接口出现异常时不会输出异常日志
     * @param point 切入点重要参数
     * @return 返回方法执行之后的方法返回值
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 必要的一些初始化
        beforeRequiredInit(point);

        // 如果该方法并没有Http相关的请求对象则直接自行方法不输出请求和响应日志
        if (this.attributes == null) {
            log.warn("current invoke method'" + invokeMethod.getName() + "()'not have request bind, This may be an internal call.");
            return point.proceed();
        }

        // 输出日志
        outputLogger(HttpSchedule.REQUEST);

        // 执行目标方法
        setResult(point.proceed());

        // 设置响应对象并输出日志
        setResponse(attributes.getResponse());
        outputLogger(HttpSchedule.RESPONSE);
        return this.result;
    }

    /**
     * 构建请求日志
     */
    public String buildRequestLogger() throws IOException {
        StringBuilder logger = new StringBuilder();
        setHeaders();

        // 处理并设置参数
        handlerParameter();

        logger.append("\nRequest\n")
                .append("\tid: ").append(id).append("\n")
                .append("\tpath: ").append(URLDecoder.decode(request.getRequestURI(), "UTF-8")).append("\n")
                .append("\theaders: ").append(JacksonUtils.toJSONString(this.headers)).append("\n")
                .append("\ttype: ").append(request.getMethod()).append("\n")
                .append("\tname: ").append(invokeMethod.getName());
        if (!this.pathVariable.isEmpty()) {
            if (!logger.toString().endsWith("\n")) logger.append("\n");
            logger.append("\tpathVariable: ").append(JacksonUtils.toJSONString(this.pathVariable));
        }
        if (!this.queryParam.isEmpty()) {
            if (!logger.toString().endsWith("\n")) logger.append("\n");
            logger.append("\tquery: ").append(JacksonUtils.toJSONString(this.queryParam));
        }
        if (!this.body.isEmpty()) {
            if (!logger.toString().endsWith("\n")) logger.append("\n");
            logger.append("\tbody: ").append(JacksonUtils.toJSONString(this.body));
        }
        return logger.toString();
    }

    /**
     * 处理路径参数
     */
    public void handlerParameter() {

        Parameter[] parameters = invokeMethod.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 1. PathVariable
            PathVariable pathVariableAnnotation = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariableAnnotation != null) {
                String key = pathVariableAnnotation.value();
                if (!StringUtils.hasText(key)) key = pathVariableAnnotation.name();
                pathVariable.put(key, this.args[i]);
            }
            // 2. queryParam
            RequestParam requestParamAnnotation = parameters[i].getAnnotation(RequestParam.class);
            if (requestParamAnnotation != null) {
                String key = requestParamAnnotation.value();
                String value = requestParamAnnotation.defaultValue();
                if (!StringUtils.hasText(key)) key = requestParamAnnotation.name();
                if (StringUtils.hasText(String.valueOf(this.args[i]))) value = String.valueOf(this.args[i]);
                this.queryParam.put(key, value);
            }
            // 3. body
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                this.body.add(args[i]);
            }
        }
    }

    /**
     * 构建响应日志
     */
    public String buildResponseLogger() {
        StringBuilder logger = new StringBuilder();
        logger.append("\nResponse\n")
                .append("\tid: ").append(id).append("\n")
                .append("\tstatus: ").append(response.getStatus()).append("\n")
                .append("\tbody: ").append(JacksonUtils.toJSONString(result)).append("\n");
        if (apiHelper.executeTime()) logger.append("\ttime: ").append(System.currentTimeMillis() - requestTime);
        // 添加请求参数日志输出
        return logger.toString();
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setPoint(ProceedingJoinPoint point) {
        this.point = point;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public void setAttributes(ServletRequestAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * 输出日志
     */
    public void outputLogger(HttpSchedule schedule) throws IOException {
        // 构建日志
        switch (schedule) {
            case REQUEST:
                if (apiHelper.enableRequest()) {
                    String requestLogger = buildRequestLogger();
                    log.info(requestLogger);
                }
                break;
            case RESPONSE:
                if (apiHelper.enableResponse()) {
                    String responseLogger = buildResponseLogger();
                    log.info(responseLogger);
                }
                break;
        }
    }


    /**
     * 必要的前置数据初始化
     */
    public void beforeRequiredInit(ProceedingJoinPoint point) {

        // 请求时间
        setRequestTime(System.currentTimeMillis());
        id = UUID.randomUUID().toString().replaceAll("-", "");
        setPoint(point);

        // 重置参数对象
        resetParameters();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        setAttributes(attributes);

        if (this.attributes != null) {
            setRequest(this.attributes.getRequest());
        }

        this.args = point.getArgs();

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        setInvokeMethod(method);

        APIHelper apiHelper = method.getAnnotation(APIHelper.class);
        setApiHelper(apiHelper);

        setLogOutputType(apiHelper.output());
    }

    private void resetParameters() {
        this.body = new ForgeBody();
        this.queryParam = new ForgeQueryParam();
        this.pathVariable = new ForgePathVariable();
        this.headers = new ForgeHeader();
    }

    public void setInvokeMethod(Method invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public void setApiHelper(APIHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    public void setHeaders() {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
    }
    public ForgeHeader getHeaders() {
        return this.headers;
    }

    public void setLogOutputType(LogOutputType logOutputType) {
        this.logOutputType = logOutputType;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Object getResult() {
        return result;
    }

    public ProceedingJoinPoint getPoint() {
        return point;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public ServletRequestAttributes getAttributes() {
        return attributes;
    }

    public Method getInvokeMethod() {
        return invokeMethod;
    }

    public APIHelper getApiHelper() {
        return apiHelper;
    }

    public LogOutputType getLogOutputType() {
        return logOutputType;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public ForgePathVariable getPathVariable() {
        return pathVariable;
    }

    private void setPathVariable(ForgePathVariable pathVariable) {
        this.pathVariable = pathVariable;
    }

    public ForgeQueryParam getQueryParam() {
        return queryParam;
    }

    private void setQueryParam(ForgeQueryParam queryParam) {
        this.queryParam = queryParam;
    }

    public ForgeBody getBody() {
        return body;
    }

    private void setBody(ForgeBody body) {
        this.body = body;
    }

    public Object[] getArgs() {
        return args;
    }
}

package com.bnyte.forge.aop.actuator;

import com.bnyte.forge.annotation.APIHelper;
import com.bnyte.forge.enums.HttpSchedule;
import com.bnyte.forge.enums.LogOutputType;
import com.bnyte.forge.util.JacksonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object result;
    private ProceedingJoinPoint point;
    private long requestTime;
    private ServletRequestAttributes attributes;
    private Method invokeMethod;
    private APIHelper apiHelper;
    private String headers;
    private LogOutputType logOutputType;
    private String id;

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
            log.warn("提醒开发当前API没有HTTP Request对象");
            return point.proceed();
        }

        // 输出日志
        outputLogger(HttpSchedule.REQUEST);

        // 执行目标方法
        setResult(point.proceed());

        // 设置响应对象并输出日志
        setResponse(attributes.getResponse());
        outputLogger(HttpSchedule.RESPONSE);
        return point.proceed();
    }

    /**
     * 构建请求日志
     */
    public String buildRequestLogger() {
        StringBuilder logger = new StringBuilder();
        setHeaders();
        logger.append("\nRequest\n")
                .append("\tid: ").append(id).append("\n")
                .append("\tpath: ").append(request.getRequestURI()).append("\n")
                .append("\theaders: ").append(headers).append("\n")
                .append("\ttype: ").append(request.getMethod()).append("\n")
                .append("\tname: ").append(invokeMethod.getName());
        // 添加请求参数日志输出
        return logger.toString();
    }

    /**
     * 构建响应日志
     */
    public String buildResponseLogger() {
        StringBuilder logger = new StringBuilder();
        logger.append("\nResponse\n")
                .append("\tid: ").append(id).append("\n")
                .append("\tstatus: ").append(response.getStatus()).append("\n")
                .append("\tbody: ").append(result).append("\n");
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
    public void outputLogger(HttpSchedule schedule) {
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

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        setAttributes(attributes);

        if (this.attributes != null) {
            setRequest(this.attributes.getRequest());
        }

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        setInvokeMethod(method);

        APIHelper apiHelper = method.getAnnotation(APIHelper.class);
        setApiHelper(apiHelper);

        setLogOutputType(apiHelper.output());
    }

    public void setInvokeMethod(Method invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public void setApiHelper(APIHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    public void setHeaders() {
        Map<String, Object> header = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        Iterator<String> headerNameIterator = headerNames.asIterator();
        while (headerNameIterator.hasNext()) {
            String headerName = headerNameIterator.next();
            header.put(headerName, request.getHeader(headerName));
        }

        // 获取响应方式
        switch (logOutputType) {
            case JSON:
                headers = JacksonUtils.toJSONString(header);
                break;
            case TO_STRING: header.toString();
                break;

        }

        this.headers = headers;
    }

    public String getHeaders() {
        return "headers";
    }

    public void setLogOutputType(LogOutputType logOutputType) {
        this.logOutputType = logOutputType;
    }

}

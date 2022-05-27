package com.bnyte.forge.http.reactive.web;

import java.io.Serializable;

/**
 * web统一响应结果集合
 *  已经启用该项目, 可以查看 https://github.com/bnyte/xuni
 *  预计在1.0.9移除改版本
 * @auther bnyte
 * @date 2021-12-09 18:42
 * @email bnytezz@gmail.com
 */
@Deprecated
public enum Status implements Serializable {
    ok(0, "succeeded"),
    error(-1, "failed"),
    ;
    private static final long serialVersionUID = 94264211209183701L;

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

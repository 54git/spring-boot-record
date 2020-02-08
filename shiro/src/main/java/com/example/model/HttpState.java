package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;

/**
 * http状态码枚举
 * org.springframework.http.HttpStatus
 */
@Slf4j
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HttpState {

    CONTINUE(100, "请继续发送请求的剩余部分"),
    SWITCHING_PROTOCOLS(101, "协议切换"),
    PROCESSING(102, "请求将继续执行"),
    CHECKPOINT(103, "可以预加载"),
    OK(200, "请求已经成功处理"),
    CREATED(201, "请求已经成功处理 并创建了资源"),
    ACCEPTED(202, "请求已经接受 等待执行"),
    NON_AUTHORITATIVE_INFORMATION(203, "请求已经成功处理 但是信息不是原始的"),
    NO_CONTENT(204, "请求已经成功处理 没有内容需要返回"),
    RESET_CONTENT(205, "请求已经成功处理 请重置视图"),
    PARTIAL_CONTENT(206, "部分Get请求已经成功处理"),
    MULTI_STATUS(207, "请求已经成功处理 将返回XML消息体"),
    ALREADY_REPORTED(208, "请求已经成功处理 一个DAV的绑定成员被前一个请求枚举 并且没有被再一次包括"),
    IM_USED(226, "请求已经成功处理 将响应一个或者多个实例"),
    MULTIPLE_CHOICES(300, "提供可供选择的回馈"),
    MOVED_PERMANENTLY(301, "请求的资源已经永久转移"),
    FOUND(302, "请重新发送请求"),
    SEE_OTHER(303, "请以Get方式请求另一个URI"),
    NOT_MODIFIED(304, "资源未改变"),
    USE_PROXY(305, "请通过Location域中的代理进行访问"),
    // 306在新版本的规范中被弃用
    TEMPORARY_REDIRECT(307, "请求的资源临时从不同的URI响应请求"),
    RESUME_INCOMPLETE(308, "请求的资源已经永久转移"),
    BAD_REQUEST(400, "请求错误 请修正请求"),
    UNAUTHORIZED(401, "没有被授权或者授权已经失效"),
    PAYMENT_REQUIRED(402, "预留状态"),
    FORBIDDEN(403, "请求被理解 但是拒绝执行"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许被执行"),
    NOT_ACCEPTABLE(406, "请求的资源不满足请求者要求"),
    PROXY_AUTHENTICATION_REQUIRED(407, "请通过代理进行身份验证"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "请求冲突"),
    GONE(410, "请求的资源不可用"),
    LENGTH_REQUIRED(411, "Content-Length未定义"),
    PRECONDITION_FAILED(412, "不满足请求的先决条件"),
    REQUEST_ENTITY_TOO_LARGE(413, "请求发送的实体太大"),
    REQUEST_URI_TOO_LONG(414, "请求的URI超长"),
    UNSUPPORTED_MEDIA_TYPE(415, "请求发送的实体类型不受支持"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Range指定的范围与当前资源可用范围不一致"),
    EXPECTATION_FAILED(417, "请求头Expect中指定的预期内容无法被服务器满足"),
    UNPROCESSABLE_ENTITY(422, "请求格式正确 但是由于含有语义错误 无法响应"),
    LOCKED(423, "当前资源被锁定"),
    FAILED_DEPENDENCY(424, "由于之前的请求发生错误 导致当前请求失败"),
    UPGRADE_REQUIRED(426, "客户端需要切换到TLS1.0"),
    PRECONDITION_REQUIRED(428, "请求需要提供前置条件"),
    TOO_MANY_REQUESTS(429, "请求过多"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "请求头超大 拒绝请求"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "服务器不支持当前请求的部分功能"),
    BAD_GATEWAY(502, "响应无效"),
    SERVICE_UNAVAILABLE(503, "服务器维护或者过载 拒绝服务"),
    GATEWAY_TIMEOUT(504, "上游服务器超时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "不支持的HTTP版本"),
    VARIANT_ALSO_NEGOTIATES(506, "服务器内部配置错误"),
    INSUFFICIENT_STORAGE(507, "服务器无法完成存储请求所需的内容"),
    LOOP_DETECTED(508, "服务器处理请求时发现死循环"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "服务器达到带宽限制"),
    NOT_EXTENDED(510, "获取资源所需的策略没有被满足"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "需要进行网络授权");

    private final int code;
    private final String message;

    HttpState(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    // int到enum的转换函数
    public static HttpState valueOf(int code) {
//        // 枚举值较少时可以使用switch
//        switch (code) {
//            case 100:
//                return CONTINUE;
//            case 200:
//                return OK;
//            default:
//                return null;
//        }

        for (HttpState httpState : values()) {
            if (httpState.code() == code)
                return httpState;
        }

        log.error("No matching constant for [{}]", code);
        return HttpState.EXPECTATION_FAILED;
    }

    private static final int
            INFORMATIONAL = 1, SUCCESSFUL = 2, REDIRECTION = 3, CLIENT_ERROR = 4, SERVER_ERROR = 5;

    public boolean is1xxInformational() {
        return type() == INFORMATIONAL;
    }

    public boolean is2xxSuccessful() {
        return type() == SUCCESSFUL;
    }

    public boolean is3xxRedirection() {
        return type() == REDIRECTION;
    }

    public boolean is4xxClientError() {
        return type() == CLIENT_ERROR;
    }

    public boolean is5xxServerError() {
        return type() == SERVER_ERROR;
    }

    private int type() {
        return code / 100;
    }
}
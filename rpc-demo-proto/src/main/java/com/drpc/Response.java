package com.drpc;

import lombok.Data;

/**
 * 表示响应
 */
@Data
public class Response {
    //返回类型，0表示成功，非0表示失败
    private int code;
    //返回信息
    private String message;
    //返回数据
    private Object data;
}

package com.drpc;

import lombok.Data;

/***
 * 表示一个请求，请求的服务以及服务的参数
 */
@Data
public class Request {
    private ServiceDescriptor service;
    private Object[] parameters;
}

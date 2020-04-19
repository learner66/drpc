package com.drpc.transport.client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理客户端的请求
 */
public interface RequestHandler {
    /**
     * 处理请求
     * @param receive 请求信息
     * @param toResp  返回信息
     */
    void onRequest(InputStream receive , OutputStream toResp);
}

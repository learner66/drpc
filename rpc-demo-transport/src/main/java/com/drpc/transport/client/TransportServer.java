package com.drpc.transport.client;

/**
 * 服务的提供者
 */
public interface TransportServer {
    /**
     * 服务端初始化，启动端口进行监听
     * @param port 端口信息
     * @param handler 处理请求的函数
     */
    void init(int port ,RequestHandler handler);

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();
}

package com.drpc.transport.client;

import com.drpc.Peer;
import java.io.InputStream;

/**
 *  连接的客户端，服务的请求者称为客户端，客户端会主动连接服务端，并且从向服务端表明所需要的服务类型
 *  并且调用该服务进行运算。
 */
public interface TransportClient {
    /**
     * 连接到服务端
     * @param peer 服务端地址
     */
    void connect(Peer peer);

    /**
     * 将所需要的数据传递给服务端，即将自己需要的方法的信息传递给服务端
     * @param data 数据
     * @return 处理结果
     */
    InputStream write (InputStream data);

    /**
     * 关闭连接
     */
    void close();
}

package com.drpc.client;

import com.drpc.codec.Decoder;
import com.drpc.codec.Encoder;
import com.drpc.common.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * RPC的客户端
 */
public class RpcClient {
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;
    public RpcClient(){
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config){
        this.config = config;
        this.encoder = ReflectionUtils.newInstance(this.config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(this.config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(this.config.getSelectorClass());
        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
    }

    // 获得一个代理对象
    public <T> T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz,encoder,decoder,selector));
    }
}

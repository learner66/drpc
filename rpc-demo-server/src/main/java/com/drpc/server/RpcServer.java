package com.drpc.server;

import com.drpc.Request;
import com.drpc.Response;
import com.drpc.codec.Decoder;
import com.drpc.codec.Encoder;
import com.drpc.common.utils.ReflectionUtils;
import com.drpc.transport.client.RequestHandler;
import com.drpc.transport.client.TransportServer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RPC的Server实现，用来接受RPC请求
 */
@Data
@Slf4j
public class RpcServer {
    // 服务器配置
    private RpcServerConfig config;
    // 服务器
    private TransportServer net;
    // 序列化
    private Encoder encoder;
    // 反序列化
    private Decoder decoder;
    // 服务管理
    private ServiceManager serviceManager;
    // 服务启动
    private ServiceInvoker serviceInvoker;

    public RpcServer() {
        this(new RpcServerConfig());
    }
    //通过反射来创建对象
    public RpcServer(RpcServerConfig config){
        this.config = config;
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(),this.handler);
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();

    }

    //服务注册
    public <T> void register(Class<T> interfaceClass,T bean){
        serviceManager.register(interfaceClass,bean);
    }

    //服务端启动
    public void start(){
        this.net.start();
    }
    //服务端停止
    public void stop(){
        this.net.stop();
    }

    //RequestHandler实现
    private RequestHandler handler = new RequestHandler(){
        @Override
        public void onRequest(InputStream receive, OutputStream toResp) {
            Response resp = new Response();
            try {
                // 从InputStream中读取数据
                byte[] inBytes = IOUtils.readFully(receive, receive.available());

                // 将读取的二进制数据转为Request对象
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request:{}",request);
                //寻找服务
                ServiceInstance sis = serviceManager.lookup(request);
                //调用服务，返回结果
                Object ret = serviceInvoker.invoke(sis,request);
                // 返回结果
                resp.setData(ret);
            } catch (Exception e) {
               log.warn(e.getMessage(),e);
               resp.setCode(1);
               resp.setMessage("RpcServer get error: "+e.getClass().getName()+ " : " +e.getMessage());
            }finally {
                try {
                    //将resp序列化
                    byte[] outBytes = encoder.encode(resp);
                    //写入到输出字符流
                    toResp.write(outBytes);
                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(),e);
                }
            }
        }
    };
}

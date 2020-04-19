package com.drpc.client;

import com.drpc.Request;
import com.drpc.Response;
import com.drpc.ServiceDescriptor;
import com.drpc.codec.Decoder;
import com.drpc.codec.Encoder;
import com.drpc.transport.client.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 */
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;
    RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder,TransportSelector selector){
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
    }

    /**
     * 执行远程服务
     * @param proxy 代理
     * @param method 方法
     * @param args  参数
     * @return 返回远程服务的执行结果
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz,method));
        request.setParameters(args);
        Response resp = invokeRemote(request);
        if(resp==null||resp.getCode()!=0){
            throw new IllegalStateException("fail to invoke remote: "+resp);
        }
        return resp.getData();
    }

    /**
     * 调用服务
     * @param request 请求
     * @return 处理结果
     */
    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null;
        try{
            // 随机选择一个连接来处理该请求
            client = selector.select();
            // 将请求进行序列化
            byte[] outBytes = encoder.encode(request);
            // 请求写入到连接中
            InputStream receive = client.write(new ByteArrayInputStream(outBytes));
            byte[] inBytes = IOUtils.readFully(receive,
                    receive.available());
            // 将接受到的数据进行反序列化
            resp = decoder.decode(inBytes,Response.class);

        } catch (IOException e) {
            //log.warn(e.getMessage(),e);
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error: "+ e.getClass() + " : "+ e.getMessage());
        } finally {
            if(client!=null) {
                selector.release(client);
            }
        }
        return resp;
    }
}

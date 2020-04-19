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

@Data
@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;

    public RpcServer() {
        this(new RpcServerConfig());
    }
    public RpcServer(RpcServerConfig config){
        this.config = config;
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(),this.handler);
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();

    }
    public <T> void register(Class<T> interfaceClass,T bean){
        serviceManager.register(interfaceClass,bean);
    }
    public void start(){
        this.net.start();
    }
    public void stop(){
        this.net.stop();
    }
    private RequestHandler handler = new RequestHandler(){
        @Override
        public void onRequest(InputStream receive, OutputStream toResp) {
            Response resp = new Response();
            //TODO
            try {
                byte[] inBytes = IOUtils.readFully(receive, receive.available());

                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request:{}",request);
                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoke(sis,request);
                // not done
                resp.setData(ret);
            } catch (Exception e) {
               log.warn(e.getMessage(),e);
               resp.setCode(1);
               resp.setMessage("RpcServer get error: "+e.getClass().getName()+ " : " +e.getMessage());
            }finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);
                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(),e);
                }
            }
        }
    };
}

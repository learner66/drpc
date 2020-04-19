package com.drpc.server;

import com.drpc.Request;
import com.drpc.ServiceDescriptor;
import com.drpc.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 管理Rpc暴露的服务
 */
@Slf4j
public class ServiceManager {
    private Map<ServiceDescriptor,ServiceInstance> services;
    public ServiceManager(){
        this.services = new ConcurrentHashMap<>();
    }
    public <T> void register(Class<T> interfaceClass,T bean){
       Method[] methods =  ReflectionUtils.getPublicMethods(interfaceClass);
       for(Method method:methods){
           ServiceInstance sis = new ServiceInstance(bean,method);
           ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass,method);
           services.put(sdp,sis);
           log.info("register service:{} {}",sdp.getClazz(),sdp.getMethod());
       }

    }


    public ServiceInstance lookup(Request request){
        ServiceDescriptor sdp = request.getService();
        return services.get(sdp);
    }
}

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
    // 服务和服务实体的map
    private Map<ServiceDescriptor,ServiceInstance> services;
    public ServiceManager(){
        this.services = new ConcurrentHashMap<>();
    }
    public <T> void register(Class<T> interfaceClass,T bean){
       //得到服务所在类的所有公有方法
       Method[] methods =  ReflectionUtils.getPublicMethods(interfaceClass);
       for(Method method:methods){
           //将服务实例化， 类+方法
           ServiceInstance sis = new ServiceInstance(bean,method);
           //生成一个服务描述
           ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass,method);
           //将服务描述和服务实例放入到map中，即是完成注册
           services.put(sdp,sis);
           log.info("register service:{} {}",sdp.getClazz(),sdp.getMethod());
       }

    }

    public ServiceInstance lookup(Request request){
        // 得到请求对应的服务描述
        ServiceDescriptor sdp = request.getService();
        // 从services返回对应的服务实例
        return services.get(sdp);
    }
}

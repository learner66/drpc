package com.drpc.server;

import com.drpc.Request;
import com.drpc.common.utils.ReflectionUtils;

/**
 * 服务执行
 */
public class ServiceInvoker {
    /**
     * 执行服务
     * @param service 服务的实例
     * @param request 服务的请求者
     * @return 执行结果
     */
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),request.getParameters());
    }
}

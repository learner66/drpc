package com.drpc.server;

import com.drpc.Request;
import com.drpc.common.utils.ReflectionUtils;

public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),request.getParameters());
    }
}

package com.drpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * 服务实例
 * target代表服务目标类
 * method代表实际的方法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}

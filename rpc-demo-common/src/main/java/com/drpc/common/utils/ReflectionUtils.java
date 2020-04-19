package com.drpc.common.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public  class ReflectionUtils {
    /**
     * 根据class创建对象
     * @param clazz 待创建对象
     * @param <T> 对象类型
     * @return 创建好的对象
     */
    public static <T> T newInstance(Class<T> clazz){
        try {
            //利用newInstance来创建新对象
            return clazz.newInstance();
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }

    public static Method[] getPublicMethods(Class clazz){
        //getDeclaredMethods可以获取所有的方法，包含私有的方法
        Method[] methods = clazz.getDeclaredMethods();
        //pmethods只存储私有的方法
        List<Method> pmethods = new ArrayList<>();
        for(Method m : methods){
            //选择所有的公有方法
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj 被调用的对象
     * @param method 被调用的方法
     * @param args 被调用的参数
     * @return
     */
    public static Object invoke(Object obj,Method method, Object... args){
        try {
           //调用指定对象的指定方法
           return method.invoke(obj,args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

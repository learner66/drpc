package com.drpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 表示服务，其实就是方法，包含了方法所处在的类，方法的名称，方法的返回类型以及方法的参数类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor {
   //类名
   private String clazz;
   //方法
   private String method;
   //返回类型
   private String returnType;
   //参数类型
   private String[] parameterTypes;

   /**
    * 生成一个服务
    * @param clazz 服务所处的对象
    * @param method 服务的方法
    * @return 服务
    */
   public static ServiceDescriptor from(Class clazz, Method method){
      ServiceDescriptor sdp = new ServiceDescriptor();
      //设置类
      sdp.setClazz(clazz.getName());
      //设置方法
      sdp.setMethod(method.getName());
      // 设置返回类型
      sdp.setReturnType(method.getReturnType().getName());
      //得到所有参数类型
      Class[] parameterClasses = method.getParameterTypes();
      String[] parameterTypes = new String[parameterClasses.length];
      for(int i = 0;i<parameterClasses.length;i++){
         parameterTypes[i] = parameterClasses[i].getName();
      }
      //设置参数类型
      sdp.setParameterTypes(parameterTypes);
      return sdp;
   }

   @Override
   public int hashCode(){
      return toString().hashCode();
   }
   @Override
   public boolean equals(Object obj){
      if(this==obj){
         return true;
      }
      if(obj==null||this.getClass()!=obj.getClass()){
         return false;
      }
      ServiceDescriptor that = (ServiceDescriptor)obj;
      return this.toString().equals(that.toString());
   }

   @Override
   public String toString(){
      return "Class: "+ clazz +
              ",Method: "+ method +
              ",return: "+ returnType +
              ",ParamterTypes: "+ Arrays.toString(parameterTypes);
   }
}



























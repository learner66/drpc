package com.drpc;


import com.sun.org.glassfish.gmbal.ManagedAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  表示一个端点
 */
//get和set方法，以及toString方法
@Data
@AllArgsConstructor
public class Peer {
    // 表示主机地址
    private String host;
    // 表示端口
    private int port;
}

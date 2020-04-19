package com.drpc.client;

import com.drpc.Peer;
import com.drpc.common.utils.ReflectionUtils;
import com.drpc.transport.client.TransportClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 随机选择器
 */
@Slf4j
public class RandomTransportSelector implements TransportSelector {
    /**
     * 已经连接好的client
     */
    private List<TransportClient> clients;

    //利用CopyWriteArrayList保存服务
    public RandomTransportSelector(){
        this.clients = new CopyOnWriteArrayList<>();
    }

    /**
     * 选择器初始化
     * @param peers 可以连接的server端点信息
     * @param count client与server建立多少个连接
     * @param clazz client实现class
     */
    @Override
    public void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        count = Math.max(count,1);
        // 服务端，本地只有一个服务端
        for(Peer peer:peers){
            //连接的个数
            for(int i =0;i<count;i++){
                //生成一个客户端实例
                TransportClient client = ReflectionUtils.newInstance(clazz);
                //客户端和服务端连接
                client.connect(peer);
                //保存已经连接好的客户端
                clients.add(client);
            }
            //log.info("connect server: {}",peer);
        }
    }

    /**
     * 从已经连接好的客户端中，随机选择一个
     * @return
     */
    @Override
    public synchronized  TransportClient select() {
        int i = new Random().nextInt(clients.size());
        return clients.remove(i);
    }

    /**
     * 使用完该客户端后进行释放
     * @param client
     */
    @Override
    public synchronized void release(TransportClient client) {
        clients.add(client);
    }

    /**
     * 关闭所有连接
     */
    @Override
    public synchronized void close() {
        for(TransportClient client:clients){
            client.close();
        }
        clients.clear();
    }
}

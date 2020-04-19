package com.drpc.client;

import com.drpc.Peer;
import com.drpc.codec.Decoder;
import com.drpc.codec.Encoder;
import com.drpc.codec.JSONDecoder;
import com.drpc.codec.JSONEncoder;
import com.drpc.transport.client.HTTPTransportClient;
import com.drpc.transport.client.TransportClient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 客户端配置
 * 客户端的实际实现，序列化和反序列化，服务端的地址信息，以及服务端的选择器
 */
@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass = HTTPTransportClient.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> DecoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;
    private int connectCount = 1;
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1",3000));
}

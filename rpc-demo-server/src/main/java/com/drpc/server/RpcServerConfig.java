package com.drpc.server;

import com.drpc.codec.Decoder;
import com.drpc.codec.Encoder;
import com.drpc.codec.JSONDecoder;
import com.drpc.codec.JSONEncoder;
import com.drpc.transport.client.HTTPTransportServer;
import com.drpc.transport.client.TransportServer;
import lombok.Data;

/**
 * server配置
 */

@Data
public class RpcServerConfig {
    private Class <? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class <? extends Encoder> encoderClass = JSONEncoder.class;
    private Class <? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;
}


















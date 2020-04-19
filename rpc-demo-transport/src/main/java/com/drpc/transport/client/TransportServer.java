package com.drpc.transport.client;

public interface TransportServer {
    void init(int port ,RequestHandler handler);
    void start();
    void stop();
}

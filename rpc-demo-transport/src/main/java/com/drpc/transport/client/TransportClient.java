package com.drpc.transport.client;

import com.drpc.Peer;

import java.io.InputStream;

public interface TransportClient {
    void connect(Peer peer);
    InputStream write (InputStream data);
    void close();
}

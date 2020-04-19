package com.drpc.transport.client;

import java.io.InputStream;
import java.io.OutputStream;

public interface RequestHandler {
    void onRequest(InputStream receive , OutputStream toResp);
}

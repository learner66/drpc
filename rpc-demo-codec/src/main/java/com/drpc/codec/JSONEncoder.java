package com.drpc.codec;

import com.alibaba.fastjson.JSON;

public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        //return JSON.toJSONBytes(obj);
        return JSON.toJSONString(obj).getBytes();
    }
}

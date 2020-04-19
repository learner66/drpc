package com.drpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * 基于JSON的反序列化
 */
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        //return JSON.toJSONBytes(obj);
        // 将对象转换为二进制
        return JSON.toJSONString(obj).getBytes();
    }
}

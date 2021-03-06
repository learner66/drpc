package com.drpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * 基于json的反序列化
 */
public class JSONDecoder implements Decoder {
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        // 将二进制数据转化为指定类型的对象
        return JSON.parseObject(bytes,clazz);
    }
}

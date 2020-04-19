package com.drpc.codec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONDecoderTest {
    @Test
    public void decode(){
        Encoder encoder = new JSONEncoder();
        TestBean bean = new TestBean();
        bean.setName("drpc");
        bean.setAge(19);
        byte[] bytes = encoder.encode(bean);

        Decoder decoder = new JSONDecoder();
        TestBean bean2 = decoder.decode(bytes,TestBean.class);
        assertEquals(bean.getName(),bean2.getName());
        assertEquals(bean.getAge(),bean2.getAge());
    }
}

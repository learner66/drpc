package com.drpc.codec;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JSONEncoderTest {
    @Test
    public void encode(){
        Encoder encoder = new JSONEncoder();
        TestBean bean = new TestBean();
        bean.setName("drpc");
        bean.setAge(19);
        byte[] bytes = encoder.encode(bean);
        assertNotNull(bytes);
    }
}

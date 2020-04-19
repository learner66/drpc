package com.drpc.common.utils;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectionUtilsTest {
    @Test
    public void newInstance(){
        TestClass t = ReflectionUtils.newInstance(TestClass.class);
        assertNotNull(t);
    }
    @Test
    public void methods(){
        Method[] methods = ReflectionUtils.getPublicMethods(TestClass.class);
        assertEquals(1,methods.length);
        String fname = methods[0].getName();
        assertEquals("b",fname);
    }
    @Test
    public void invoke(){
        Method[] methods = ReflectionUtils.getPublicMethods(TestClass.class);
        Method b = methods[0];
        TestClass t = new TestClass();
        Object r = ReflectionUtils.invoke(t,b);
        assertEquals("b",r);
    }
}

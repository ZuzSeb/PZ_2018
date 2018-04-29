package com.zuzseb.learning.utils.test;

import com.zuzseb.learning.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StringUtilsTest {


    @Test
    public void testStringCut() {
        Assert.assertEquals("Test12345", StringUtils.cut("Test123456789", 9));
    }
}

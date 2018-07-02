package com.rox.app.tools.ini;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Rox on 2014/7/2.
 * 测试类
 */
public class IniConfigurationFactoryTest {
    @BeforeClass
    public static void setup() {
        String configFile = getConfFileName();
        File iniFile = new File(configFile);
        if(!iniFile.exists()) {
            try {
                iniFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("Can't create the ini file for setup test environment: " + e.getLocalizedMessage());
            }
        }
    }

    @AfterClass
    public static void tearDown() {
        String configFile = getConfFileName();
        File iniFile = new File(configFile);
        if(iniFile.exists()) {
            iniFile.delete();
        }
    }

    private static String getConfFileName() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        Assert.assertNotNull(tmpdir);
        tmpdir = tmpdir.replace('\\', '/') + "test.ini";

        return tmpdir;
    }

    @Test
    public void testWriteAndReadIniFile() {
        String configFile = getConfFileName();

        IniConfigurationFactory factory = new IniConfigurationFactory(configFile);

        factory.section("storage").property("type", "jss");
        factory.section("storage").property("accessKey", "12c3ad946afa42d4bf1f2fcc22623930");
        factory.section("storage").property("sercretKey", "10f5ed1590094017b6495b493b9d67691H0W8RYk");

        factory.section("remote");

        factory.save();

        factory = new IniConfigurationFactory(configFile);
        Assert.assertNotNull(factory.section("storage"));
        Assert.assertEquals("jss", factory.section("storage").property("type").getValue());
        Assert.assertEquals("12c3ad946afa42d4bf1f2fcc22623930", factory.section("storage").property("accessKey").getValue());
        Assert.assertEquals("10f5ed1590094017b6495b493b9d67691H0W8RYk", factory.section("storage").property("sercretKey").getValue());

        Assert.assertNotNull(factory.section("remote"));
        Assert.assertNull(factory.section("remote").property("key"));
    }
}

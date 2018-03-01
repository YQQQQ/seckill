package org.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author chongyuye
 * @date 2015-12-01 21:31
 */
public class ConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    public static Properties getConfig(String filePath) {
        InputStream inputStream;
        Properties prop = new Properties();
        try {
            inputStream = ConfigUtil.class.getResourceAsStream(filePath);
            prop.load(inputStream);

            if (inputStream != null) inputStream.close();
        } catch (Exception e) {
            logger.error("init properties error: " + filePath, e);
        }
        return prop;
    }

    public static Properties getConfigProperties(){
        return ConfigUtil.getConfig("/config/config.properties");
    }

    public static void main(String[] args) {
        Properties prop = ConfigUtil.getConfig("/config/config.properties");
        logger.info(prop.getProperty("INDEX_NAME"));
    }

}

package cool.yzt.cap;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger("LogTest");

    @Test
    public void test() {
        logger.debug("测试 debug logger");
        System.out.println(logger.getName());
        logger.debug("测试 debug log");
        logger.info("测试 info log");
        logger.warn("测试 warn log");
        logger.error("测试 error log");
    }
}

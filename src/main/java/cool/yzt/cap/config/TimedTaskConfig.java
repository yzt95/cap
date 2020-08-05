package cool.yzt.cap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : yzt
 */
@EnableAsync
@EnableScheduling
@Configuration
public class TimedTaskConfig {
}

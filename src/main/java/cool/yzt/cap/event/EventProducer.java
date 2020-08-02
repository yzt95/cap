package cool.yzt.cap.event;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author : yzt
 */
@Component
public class EventProducer {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    public void triggerEvent(Event event) {
        if(event==null) {
            logger.error("不可以发送空消息");
            return;
        }
        kafkaTemplate.send(event.getTopic(), JSONUtil.toJsonStr(event));
    }
}

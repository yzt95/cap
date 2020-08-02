package cool.yzt.cap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * @author : yzt
 */
@SpringBootTest
public class KafkaTest {
    @Autowired
    private Producer producer;

    @Test
    public void test() {
        producer.send("test","hello,kafka!");
        producer.send("test","hello,spring!");
        producer.send("test","hello,world!");
        System.out.println("ok");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

@Component
class Producer {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String topic,String content) {
        kafkaTemplate.send(topic,content);
    }
}

@Component
class Consumer {
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord<String,String> record){
        System.out.println("test");
        String value = record.value();
        System.out.println(value);
    }
}

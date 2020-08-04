package cool.yzt.cap.event;

import java.util.HashMap;
import java.util.Map;

/** 触发系统消息发送的事件
 * @author : yzt
 */
public class Event {

    /**
     * kafka topic
     */
    private String topic;

    /**
     * 触发的用户
     */
    private int triggerUserId;

    /**
     * 目标用户
     */
    private int targetUserId;

    /**
     * 其余信息放在map中
     */
    private Map<String,String> data ;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTriggerUserId() {
        return triggerUserId;
    }

    public void setTriggerUserId(int triggerUserId) {
        this.triggerUserId = triggerUserId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

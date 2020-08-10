package cool.yzt.cap.event;

import cn.hutool.json.JSONUtil;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.SystemNotice;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.util.SystemNoticeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : yzt
 */

@Component
public class EventConsumer implements MessageConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private SystemNoticeService systemNoticeService;

    /**
     * 评论、点赞、关注事件
     */
    /*
    @KafkaListener(topics = {EVENT_COMMENT,EVENT_LIKE,EVENT_FOLLOW})
    public void handleSystemNotice(ConsumerRecord<String,String> record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容不可以为空");
            return;
        }

        Event event = JSONUtil.toBean(record.value(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        SystemNotice notice = new SystemNotice();
        notice.setToId(event.getTargetUserId());
        notice.setStatus(0);
        notice.setCreateTime(new Date());
        if(event.getTopic().equals(EVENT_COMMENT)) {
            notice.setContent(SystemNoticeUtil.getCommentAndLikeNoticeContent(event.getData()));
            notice.setType(SYSTEM_NOTICE_TYPE_COMMENT);
        } else if (event.getTopic().equals(EVENT_LIKE)) {
            notice.setType(SYSTEM_NOTICE_TYPE_LIKE);
            notice.setContent(SystemNoticeUtil.getCommentAndLikeNoticeContent(event.getData()));
        } else {
            notice.setType(SYSTEM_NOTICE_TYPE_FOLLOW);
            notice.setContent(SystemNoticeUtil.getFollowNoticeContent(event.getData()));
        }
        systemNoticeService.save(notice);
    }
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "system_notice",type = "direct"),
            key = {EVENT_COMMENT,EVENT_LIKE,EVENT_FOLLOW}
    ))
    public void receiveMessage(String message) {
        if (StringUtils.isBlank(message)) {
            logger.error("消息的内容不可以为空");
            return;
        }

        Event event = JSONUtil.toBean(message, Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        SystemNotice notice = new SystemNotice();
        notice.setToId(event.getTargetUserId());
        notice.setStatus(0);
        notice.setCreateTime(new Date());
        if(event.getTopic().equals(EVENT_COMMENT)) {
            notice.setContent(SystemNoticeUtil.getCommentAndLikeNoticeContent(event.getData()));
            notice.setType(SYSTEM_NOTICE_TYPE_COMMENT);
        } else if (event.getTopic().equals(EVENT_LIKE)) {
            notice.setType(SYSTEM_NOTICE_TYPE_LIKE);
            notice.setContent(SystemNoticeUtil.getCommentAndLikeNoticeContent(event.getData()));
        } else {
            notice.setType(SYSTEM_NOTICE_TYPE_FOLLOW);
            notice.setContent(SystemNoticeUtil.getFollowNoticeContent(event.getData()));
        }
        systemNoticeService.save(notice);
    }

}

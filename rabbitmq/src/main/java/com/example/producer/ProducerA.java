package com.example.producer;

import com.example.util.RabbitMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.example.config.RabbitMQConfig.FANOUT_EXCHANGE;

/**
 * @author 李磊
 * @since 1.0
 */
@Slf4j
@Component
public class ProducerA implements RabbitTemplate.ConfirmCallback {

    private final RabbitTemplate rabbitTemplate;

    public ProducerA(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 设置回调函数
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendAll(String content) {
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, null
                , content, RabbitMQUtil.id());
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("A生产者回调id " + correlationData);
        if (ack) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败 " + cause);
        }
    }
}
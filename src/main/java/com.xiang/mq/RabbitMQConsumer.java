package cn.itcast.mq;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

/**
 * @Auther 风间影月
 */
@Component
@Slf4j
public class RabbitMQConsumer {

    @Resource
    private ChatMessageService chatMessageService;

    @RabbitListener(queues = {RabbitMQTestConfig.TEST_QUEUE})
    public void watchQueue(String payload, Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = " + routingKey);

        if (routingKey.equals(RabbitMQTestConfig.ROUTING_KEY_WECHAT_MSG_SEND)) {
            String msg = payload;
            ChatMsg chatMsg = JsonUtils.jsonToPojo(msg, ChatMsg.class);

            chatMessageService.saveMsg(chatMsg);
        }

    }

}

package com.xiang.quickim;

import com.xiang.message.ChatRequestMessage;
import com.xiang.message.ChatResponseMessage;
import com.xiang.server.handler.ChatRequestMessageHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatServerTest {
    @Test
    public void testPrivateChat() {
        EmbeddedChannel serverChannel = new EmbeddedChannel(new ChatRequestMessageHandler());

        // 模拟 User1 发送消息给 User2
        ChatRequestMessage msg = new ChatRequestMessage("user1", "user2", "Hello, user2!");
        serverChannel.writeInbound(msg);

        // 断言消息是否正确处理
        ChatResponseMessage response = serverChannel.readOutbound();
        assertNotNull(response);
        assertEquals("user1", response.getFrom());
        assertEquals("Hello, user2!", response.getContent());
    }
}

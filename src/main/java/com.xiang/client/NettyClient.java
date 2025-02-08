package cn.itcast.client;

import cn.itcast.protocol.ProcotolFrameDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class NettyClient {
    private Channel channel;
    
    @PostConstruct
    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group,)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    // 添加编解码器、心跳等处理器（参考原代码）
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                    ch.pipeline().addLast(new HeartbeatHandler());
                    ch.pipeline().addLast(new ClientMessageHandler());
                }
            });
        
        // 连接 Netty 服务器
        ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
        this.channel = future.channel();
    }

    // 发送消息到 Netty Server
    public void sendMessage(Message message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
    }
}
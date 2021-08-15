package com.qin.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/22 22:43.
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println("[Handler] [" + System.currentTimeMillis() + "] : " + messageProtocol.toString());
        System.out.println("收到消息：" + new String(messageProtocol.getContent(), StandardCharsets.UTF_8));
        String uuid = UUID.randomUUID().toString();
        byte[] bytes = uuid.getBytes(StandardCharsets.UTF_8);
        MessageProtocol message = new MessageProtocol();
        message.setLength(bytes.length);
        message.setContent(bytes);
        channelHandlerContext.writeAndFlush(message);
    }
}

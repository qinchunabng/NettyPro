package com.qin.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/22 23:09.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Scanner scanner = new Scanner(System.in);
//        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            byte[] content = line.getBytes(StandardCharsets.UTF_8);
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLength(content.length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
//        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println("[Handler] [" + System.currentTimeMillis() + "] : " + messageProtocol.toString());
        System.out.println("收到消息：" + new String(messageProtocol.getContent(), StandardCharsets.UTF_8));
    }
}

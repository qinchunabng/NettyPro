package com.qin.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/22 22:54.
 */
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol, ByteBuf byteBuf) throws Exception {
        System.out.println("[Encoder] [" + System.currentTimeMillis() + "] : " + messageProtocol.toString());
        byteBuf.writeInt(messageProtocol.getLength());
        byteBuf.writeBytes(messageProtocol.getContent());
    }
}

package com.qin.netty.advance.test;

import com.qin.netty.advance.message.LoginRequestMessage;
import com.qin.netty.advance.message.Message;
import com.qin.netty.advance.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

/**
 * description
 *
 * @author DELL
 * @date 2021/08/15 17:18.
 */
public class TestMessageCodec {

    public static void main(String[] args) throws IOException {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new MessageCodec());

        //encode
        Message loginMessage = new LoginRequestMessage("zhangsan","123","张三");
        channel.writeOutbound(loginMessage);

        //decode
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().toByteBuf(loginMessage, byteBuf);
        channel.writeInbound(byteBuf);
    }
}

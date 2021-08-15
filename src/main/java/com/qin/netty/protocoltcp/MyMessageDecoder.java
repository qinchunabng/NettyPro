package com.qin.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/22 22:46.
 */
public class MyMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() > 4){
            int length = byteBuf.readInt();
            byte[] content = new byte[length];
            byteBuf.readBytes(content);

            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLength(length);
            messageProtocol.setContent(content);
            System.out.println("[Decoder] [" + System.currentTimeMillis() + "] : " + messageProtocol.toString());
            list.add(messageProtocol);
        }
    }
}

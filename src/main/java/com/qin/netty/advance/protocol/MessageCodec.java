package com.qin.netty.advance.protocol;

import com.qin.netty.advance.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 消息编解码器
 *
 * @author DELL
 * @date 2021/08/15 16:39.
 */
public class MessageCodec extends ByteToMessageCodec<Message> {

    private final Logger logger = LoggerFactory.getLogger(MessageCodec.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        toByteBuf(message, byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magicNum = byteBuf.readInt();
        byte version = byteBuf.readByte();
        byte serializeType = byteBuf.readByte();
        byte messageType = byteBuf.readByte();
        int sequenceId = byteBuf.readInt();
        byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);
        Message message = null;
        if(serializeType == 0){
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            message = (Message) ois.readObject();
        }
        logger.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializeType, messageType, sequenceId, length);
        logger.debug("{}", message);

        list.add(message);
    }

    public void toByteBuf(Message message, ByteBuf byteBuf) throws IOException {
        //4字节的魔数
        byteBuf.writeBytes(new byte[]{ 1, 2, 3, 4 });
        //1个字节的版本
        byteBuf.writeByte(1);
        //1字节的序列化方式,0:jdk,1:json
        byteBuf.writeByte(0);
        //1字节的指令类型
        byteBuf.writeByte(message.getMessageType());
        //4个字节的请求序号
        byteBuf.writeInt(message.getSequenceId());
        //对齐填充
        byteBuf.writeByte(0xff);
        //获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        //长度
        byteBuf.writeInt(bytes.length);
        //写入内容
        byteBuf.writeBytes(bytes);
    }
}

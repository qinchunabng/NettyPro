package com.qin.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/08 22:25.
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个channel组，管理所有的channel
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");

    /**
     * 表示连接建立，第一个执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将该客户加入聊天的信息推送给所有的在线的客户端
        channelGroup.writeAndFlush(simpleDateFormat.format(new Date()) + " [客户端]" + ctx.channel().remoteAddress() + "加入聊天\n");
        channelGroup.add(ctx.channel());
    }

    /**
     * 表示channel处理活动状态，提示xxx上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~\n");
    }

    /**
     * 表示channel不活动状态，提示xxx离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~\n");
    }

    /**
     * 断开连接会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //不需要手动从channelGroup remove，会自动remove channel
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
    }

    /**
     * 读取数据
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch -> {
            if(ch != channel){
                //不是当前的channel直接转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息" + s + "\n");
            }else{
                ch.writeAndFlush("[自己]发送消息" + s + "\n");
            }
        });
    }

    /**
     * 抛出异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

package com.qin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * description
 *
 * @author DELL
 * @date 2021/05/20 22:43.
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while(true){
            //等待1秒，如果没有事件发生，则返回
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //如果>0，就获取到相关的SelectionKey集合
            //通过SelectionKey获取相关的channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //根据key对应通道发生的事件做不同的处理
                if(key.isAcceptable()){
                    //如果是OP_ACCEPT，有新的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生产一个socketChannel");
                    //将socketChannel设为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    //发生OP_READ
                    //通过key获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端" + new String(buffer.array()));
                }
                //手动从集合中删除当前selectionKey，否则会出现重复操作
                keyIterator.remove();
            }
        }
    }
}


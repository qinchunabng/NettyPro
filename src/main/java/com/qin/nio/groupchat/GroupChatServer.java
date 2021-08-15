package com.qin.nio.groupchat;

import java.io.IOError;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * description
 *
 * @author DELL
 * @date 2021/05/22 12:02.
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer(){
        try {
            //得到选择器
           selector = Selector.open();
           //
           listenChannel = ServerSocketChannel.open();
           //绑定端口
           listenChannel.bind(new InetSocketAddress(PORT));
           listenChannel.configureBlocking(false);
           //注册到selector
           listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while(true){
            int count = selector.select(2000);
            if(count > 0){
                //有事件处理
                //遍历得到的SelectionKey
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while(keyIterator.hasNext()){
                    //取出SelectionKey
                    SelectionKey selectionKey = keyIterator.next();
                    if(selectionKey.isAcceptable()){
                        //监听到accept
                        SocketChannel socketChannel = listenChannel.accept();
                        socketChannel.configureBlocking(false);
                        //将socketChannel注册到selector
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        //提示
                        System.out.println(socketChannel.getRemoteAddress() + "上线");
                    }else if(selectionKey.isReadable()){
                        //通道可读，处理读事件
                        readData(selectionKey);
                    }
                    //删除当前key，防止重复操作
                    keyIterator.remove();
                }
            }else{
                System.out.println("等待连接...");
            }
        }
    }

    /**
     * 读取客户端消息
     * @param key
     */
    private void readData(SelectionKey key) throws IOException {
        //得到关联的channel
        SocketChannel socketChannel=null;
        try {
            socketChannel = (SocketChannel) key.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readLength = socketChannel.read(buffer);
            if (readLength > 0) {
                String msg = new String(buffer.array());
                //输出消息
                System.out.println("from客户端：" + msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg, socketChannel);
            }
        }catch (IOException e){
            try {
                if(socketChannel != null) {
                    System.out.println(socketChannel.getRemoteAddress() + "离线了");
                    //取消注册
                    key.cancel();
                    //关闭通道
                    socketChannel.close();
                }
            }catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端（通道）
     * @param msg
     * @param self
     */
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("消息转发中...");
        //遍历所有注册到selector上的SocketChannel，并排除self
        for(SelectionKey key : selector.keys()){
            //通过key取出对应的SocketChannel
            Channel channel = key.channel();
            //排除自己
            if(channel instanceof SocketChannel && channel != self){
                //
                SocketChannel socketChannel = (SocketChannel) channel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer写入通道
                socketChannel.write(buffer);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}

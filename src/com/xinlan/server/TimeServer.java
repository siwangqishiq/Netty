package com.xinlan.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    public static final int PORT = 8888;

    public static void main(String[] agrs){
        //System.out.println("Discard");
        //EventLoopGroup group = null;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup , workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG , 1024)
                .childHandler(new TimeServer.CustomChannelInitial());

        try {
            ChannelFuture f = b.bind(PORT).sync();
            System.out.println("Server bind port : "+PORT);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private static final class CustomChannelInitial extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new TimeServerHandler());
        }
    }
}

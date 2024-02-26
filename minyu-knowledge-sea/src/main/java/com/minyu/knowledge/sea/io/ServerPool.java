package com.minyu.knowledge.sea.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2024-02-26  23:07
 */
public class ServerPool {
    private static ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException {
        //服务端启动必备
        ServerSocket serverSocket = new ServerSocket();
        //表示服务端在哪个端口上监听
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Start Server ....");
        try {
            while (true) {
                executorService.execute(new ServerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    //每个和客户端的通信都会打包成一个任务，交个一个线程来执行
    private static class ServerTask implements Runnable {

        private Socket socket = null;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //实例化与客户端通信的输入输出流
            try (ObjectInputStream inputStream =
                         new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream outputStream =
                         new ObjectOutputStream(socket.getOutputStream())) {

                //接收客户端的输出，也就是服务器的输入
                String userName = inputStream.readUTF();
                System.out.println("Accept client message:" + userName);

                //服务器的输出，也就是客户端的输入
                outputStream.writeUTF("Hello," + userName);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
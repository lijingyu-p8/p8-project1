package com.minyu.knowledge.sea.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2024-02-28  23:11
 */
public class Server {
    public static void main(String[] args) throws IOException {
        /*服务器必备*/
        ServerSocket serverSocket = new ServerSocket();
        /*绑定监听端口*/
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Server start.......");

        while (true) {
            new Thread(new ServerTask(serverSocket.accept())).start();
        }
    }

    private static class ServerTask implements Runnable {

        private Socket socket = null;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            /*拿和客户端通讯的输入输出流*/
            try (
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())
            ) {
                /*服务器的输入*/
                String userName = inputStream.readUTF();
                System.out.println("Accept clinet message:" + userName);

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

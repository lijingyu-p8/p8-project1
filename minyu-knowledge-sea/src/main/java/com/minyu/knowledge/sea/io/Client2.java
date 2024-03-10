package com.minyu.knowledge.sea.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class Client2 {
    //远程调用类
    public static IUserService getStub() throws Exception {
        //创建代理类
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("127.0.0.1", 8888);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeInt(13);
                socket.getOutputStream().write(out.toByteArray());
                socket.getOutputStream().flush();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                int ReceId = dis.readInt();
                String name = dis.readUTF();
                User user = new User(ReceId, name);
                dos.close();
                socket.close();
                return user;
            }
        };
        //执行动态代理（传入类加载器、接口、代理对象； 返回对象）
        Object o = Proxy.newProxyInstance(IUserService.class.getClassLoader(),
                new Class[]{IUserService.class}, handler);
        return (IUserService) o;
    }
}
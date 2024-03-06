package com.minyu.knowledge.sea.io;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class Client3 {
    //远程调用类
    public static Object getStub(final Class clazz) throws Exception {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("127.0.0.1", 8888);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //TODO 送入的class 灵活了
                String className = clazz.getName();
                String methodName = method.getName();
                Class[] parametersTypes = method.getParameterTypes();
                //TODO 传递class到服务器
                oos.writeUTF(className);

                oos.writeUTF(methodName);
                oos.writeObject(parametersTypes);
                oos.writeObject(args);
                oos.flush();
                //TODO 返回对象
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();
                oos.close();
                socket.close();
                return o;
            }
        };
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, handler);
        return o;
    }
}

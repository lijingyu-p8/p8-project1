package com.lijingyu.second;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        ClassLoader l = new MyClassLoader();
        Class clazz = l.loadClass("com.lijingyu.jvm.Hello");
        Class clazz1 = l.loadClass("com.lijingyu.jvm.Hello");
        System.out.println(clazz == clazz1);
        Hello h = (Hello) clazz.newInstance();
        h.m();
        System.out.println(l.getClass().getClassLoader());
        System.out.println(l.getParent());
        System.out.println(getSystemClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("c:/test/", name.replace(".", "/").concat(".class"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = fis.read()) != 0) {
                baos.write(b);
            }
            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();//可以写的更加严谨
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name);
        //throws ClassNotFoundException
    }
}
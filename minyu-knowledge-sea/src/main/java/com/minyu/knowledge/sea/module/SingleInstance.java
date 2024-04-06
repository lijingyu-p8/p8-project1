package com.minyu.knowledge.sea.module;

public class SingleInstance {
    private volatile Object object;

    public Object createInstance() {
        if (object == null) {
            synchronized (this) {
                if (object == null) {
                    object = new Object();
                }
            }
        }
        return object;
    }
}

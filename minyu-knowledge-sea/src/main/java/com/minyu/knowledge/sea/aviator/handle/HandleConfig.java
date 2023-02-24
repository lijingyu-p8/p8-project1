package com.minyu.knowledge.sea.aviator.handle;

import com.minyu.knowledge.sea.aviator.conf.ConfigResole;
import com.minyu.knowledge.sea.aviator.conf.ReCallConfig;

/**
 * @Description: 处理配置
 * @Author: lijingyu
 * @CreateTime: 2023-02-24  22:45
 */
public class HandleConfig {

    public static ReCallConfig getRecallConfig(String serviceName) {
        ConfigResole configResole = new ConfigResole();
        return configResole.getReCallConfigMap().get(serviceName);
    }

}
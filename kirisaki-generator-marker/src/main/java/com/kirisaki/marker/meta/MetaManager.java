package com.kirisaki.marker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Kirisaki
 */
public class MetaManager {
    /**
     * 使用volatile
     * 保证meta对象在多线程环境下被正确初始化
     */
    private static volatile Meta meta;

    /**
     * 构造器私有化, 防止外部创建对象
     */
    private MetaManager() {

    }

    public static Meta getMeta() {
        if (meta == null) {
            //静态文件使用当前类的字节码为锁对象
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initeMeta();
                }
            }
        }
        return meta;
    }

    /**
     * 将不同逻辑的代码进行拆分
     *
     * @return 返回模板对象
     */
    private static Meta initeMeta() {
        // 读取meta.json文件，并将其转换为字符串
        String meataJson = ResourceUtil.readUtf8Str("meta.json");
        //这段代码没理解
        Meta newMeta = JSONUtil.toBean(meataJson, Meta.class);
        //在创建对象时需要做的操作
        Meta.FileConfig fileConfig = newMeta.getFileConfig();
        return newMeta;
    }
}

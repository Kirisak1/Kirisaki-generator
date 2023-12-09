package com.kirisaki.marker.cli.command;
import cn.hutool.core.util.ReflectUtil;
import picocli.CommandLine.*;

import java.lang.reflect.Field;

/**
 * @author kirisaki
 */
@Command(name = "config", description = "获取需要输入的参数", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {
    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(GenerateCommand.class);
        for (Field field : fields) {
            String name = field.getName();
            Class<?> type = field.getType();
            System.out.println(type +"\t"+name);
        }
    }
}

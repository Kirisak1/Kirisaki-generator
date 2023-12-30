package ${basePackage}.cli.command;
import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.model.DataModel;
import picocli.CommandLine.*;

import java.lang.reflect.Field;

/**
 * @author ${author}
 */
@Command(name = "config", description = "获取需要输入的参数", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {
    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(DataModel.class);
            System.out.println("查看参数信息");
        for (Field field : fields) {
            String name = field.getName();
            Class<?> type = field.getType();
            System.out.println("字段类型:"+type +"\n"+"字段名称"+name);
            System.out.println("------------------------------------");
        }
    }
}

package ${basePackage};

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.cli.CommandExecutor;
import ${basePackage}.cli.command.GenerateCommand;
import picocli.CommandLine.Option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //这里作为调用的接口,为什么不直接在那个类里做调用接口?
        CommandExecutor commandExecutor = new CommandExecutor();
        //动态添加元素
        boolean contains = ArrayUtil.contains(args, "generate") && !ArrayUtil.contains(args, "--help");
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
        if (contains) {
            Field[] fields = ReflectUtil.getFields(GenerateCommand.class);
            for (Field field : fields) {
                Option annotation = field.getAnnotation(Option.class);
                if (annotation == null) {
                continue;
                }
                String[] names = annotation.names();
                 Boolean flag = false;
                for (String name : names) {
                    String exist = Arrays.stream(args).filter(arg -> arg.contains(name)).findFirst().orElseGet( () -> {
                        return null;
                });
                    if (exist != null) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    strings.add(names[0]);
                }
            }
        }
        //数组自动扩容?
        String[] strings1 = strings.toArray(new String[0]);
        commandExecutor.doExecute(strings1);
    }
}

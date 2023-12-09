package com.kirisaki.marker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.kirisaki.marker.generator.file.FileGenerator;
import com.kirisaki.marker.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * @author kirisaki
 */
@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {
    @Option(names = {"-a", "--author"}, description = "作者名称", echo = true, interactive = true, arity = "0..1")
    private String author = "";
    @Option(names = {"-s", "--summary"}, description = "输出结果", echo = true, interactive = true, arity = "0..1")
    private String summary = "";
    @Option(names = {"-f", "--flag"}, description = "是否开启循环", echo = true, interactive = true, arity = "0..1")
    private Boolean flag;

    @Override
    public Integer call() throws Exception {
        DataModel configTemplate = new DataModel();
        //通过this 将这个类的成员变量赋值给另一个对象
        BeanUtil.copyProperties(this, configTemplate);
        FileGenerator.doGenerator(configTemplate);
        return 0;
    }
    //作为子命令不需要写main方法, 主需要主命令处一个方法开启入口就可以
}

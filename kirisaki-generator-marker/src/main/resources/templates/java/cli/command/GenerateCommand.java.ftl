package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.FileGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * @author ${author}
 */
@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {
    <#list modelConfig.models as modelInfo>
    @Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}",</#if> "--${modelInfo.fieldName}"},<#if modelInfo.description??> description = "${modelInfo.description}",</#if> echo = true, interactive = true, arity = "0..1")
    private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
    </#list>

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

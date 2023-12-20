package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.FileGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine;
import java.util.concurrent.Callable;
<#--生成选项-->
<#macro generateOption indent modelInfo>
${indent}@Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}",</#if> "--${modelInfo.fieldName}"},<#if modelInfo.description??> description = "${modelInfo.description}",</#if> echo = true, interactive = true, arity = "0..1")
${indent}private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#macro>
<#--生成调用指令-->
<#macro generateCommand indent modelInfo>
${indent}System.out.println("输入${modelInfo.groupName}配置");
${indent}CommandLine commandLine = new CommandLine(${modelInfo.type}Command.class);
${indent}commandLine.execute(${modelInfo.allArgsStr});
</#macro>


/**
 * @author ${author}
 */
@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {
    <#list modelConfig.models as modelInfo>
    <#if modelInfo.groupKey??>
    static DataModel.${modelInfo.type} ${modelInfo.groupKey} = new DataModel.${modelInfo.type}();

    @Data
    @Command(name = "${modelInfo.groupKey}", description = "${modelInfo.groupName}", mixinStandardHelpOptions = true)
    public static class ${modelInfo.type}Command implements Runnable {
    <#list modelInfo.models as modelInfo>
        <@generateOption indent="    " modelInfo=modelInfo/>
    </#list>
    @Override
    public void run() {
        <#list modelInfo.models as subModelInfo>
        ${modelInfo.groupKey}.${subModelInfo.fieldName} = this.${subModelInfo.fieldName};
        </#list>
        }
    }
    <#else>
    <@generateOption indent="    " modelInfo=modelInfo/>
    </#if>
    </#list>
    <#--生成调用方法-->
    @Override
    public Integer call() throws Exception {
    <#list modelConfig.models as modelInfo>
    <#if modelInfo.groupKey??>
    <#if modelInfo.condition??>
        if(${modelInfo.condition}){
        <@generateCommand indent="    " modelInfo=modelInfo/>
        }
    </#if>
    </#if>
    </#list>
        DataModel configTemplate = new DataModel();
        //通过this 将这个类的成员变量赋值给另一个对象
        BeanUtil.copyProperties(this, configTemplate);
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        configTemplate.${modelInfo.groupKey} =${modelInfo.groupKey};
        </#if>
        </#list>
        FileGenerator.doGenerator(configTemplate);
        return 0;
    }
    //作为子命令不需要写main方法, 主需要主命令处一个方法开启入口就可以
}

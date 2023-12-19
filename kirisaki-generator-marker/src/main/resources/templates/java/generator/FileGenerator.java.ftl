package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class FileGenerator {
    public static void doGenerator(DataModel data) throws TemplateException, IOException {

        //生成动态文件
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";
        String input;
        String output;
    <#list fileConfig.files as fileInfo>
        <#if fileInfo.condition??>
        if (data.${fileInfo.condition}) {
            input = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
            output = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
            <#if fileInfo.generateType=="dynamic">
                DynamicGenerator.dynamic(input, output, data);
            <#else>
                StaticGenerator.copyFilesByHutool(input, output);
            </#if>
        }
        <#else>
        input = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
        output = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        <#if fileInfo.generateType=="dynamic">
            DynamicGenerator.dynamic(input, output, data);
        <#else>
            StaticGenerator.copyFilesByHutool(input, output);
        </#if>
        </#if>
    </#list>
    }
}

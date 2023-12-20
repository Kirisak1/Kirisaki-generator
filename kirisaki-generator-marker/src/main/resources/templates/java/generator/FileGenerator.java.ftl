package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

<#macro  generateFile indent fileInfo>
${indent}input = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
${indent}output = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
<#if fileInfo.generateType=="dynamic">
${indent}DynamicGenerator.dynamic(input, output, data);
<#else>
${indent}StaticGenerator.copyFilesByHutool(input, output);
</#if>
</#macro>
public class FileGenerator {
    public static void doGenerator(DataModel data) throws TemplateException, IOException {
        //生成动态文件
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        String input;
        String output;

    <#list modelConfig.models as modelInfo>
        ${modelInfo.type} ${modelInfo.fieldName} = data.${modelInfo.fieldName};
    </#list>

    <#list fileConfig.files as fileInfo>
        <#if fileInfo.groupKey??>
        <#if fileInfo.condition??>
        if (${fileInfo.condition}) {
            <#list fileInfo.files as fileInfo>
            <@generateFile indent="        " fileInfo=fileInfo/>
            </#list>
        }
        <#else>
        <#list fileInfo.files as fileInfo>
        <@generateFile indent="        " fileInfo=fileInfo/>
        </#list>
        </#if>
        <#else>
        <#if fileInfo.condition??>
        if (${fileInfo.condition}) {
            <@generateFile indent="        " fileInfo=fileInfo/>
        }
        <#else>
            <@generateFile indent="        " fileInfo=fileInfo/>
        </#if>
        </#if>
    </#list>
    }
}

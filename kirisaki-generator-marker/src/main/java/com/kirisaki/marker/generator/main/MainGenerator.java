package com.kirisaki.marker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.kirisaki.marker.generator.JarGenerator;
import com.kirisaki.marker.generator.ScriptGenerator;
import com.kirisaki.marker.generator.file.DynamciFileGenerator;
import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author Kirisaki
 * 测试调用
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMeta();
        //获取当前项目路径
        String projectPath = System.getProperty("user.dir");
        //生成文件路径
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        //判断文件夹是否存在
        if (!FileUtil.exist(outputPath)) {
            //生成目录
            FileUtil.mkdir(outputPath);
        }
        //读取resouce  不使用hutool工具包是因为hutool工具包获取的是resource内的文件内容
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
        //java包基础路径
        String basePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(basePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;
        String inputFilePath;
        String outFilePath;
        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/Main.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  DynamicFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/FileGenerator.java.ftl";
        outFilePath = outputBaseJavaPackagePath + "/generator/FileGenerator.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  StaticFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outFilePath = outputBaseJavaPackagePath + File.separator + "/generator/StaticGenerator.java";
        //生成java文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  pom
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outFilePath = outputPath + File.separator + "pom.xml";
        //生成pom文件
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);
        //构建jar包
        JarGenerator.doGenerate(outputPath);
        //生成Script脚本
        String shellOutputFilePath = outputPath + File.separator + "generator";
        //可以使用String,format进行拼接
        String jarPath = "target/" + meta.getName() + "-" + meta.getVersion() + "-jar-with-dependencies.jar";
        ScriptGenerator.doGenerate(shellOutputFilePath,jarPath);
    }
}

package com.kirisaki.marker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.kirisaki.marker.generator.GitGenerator;
import com.kirisaki.marker.generator.JarGenerator;
import com.kirisaki.marker.generator.ScriptGenerator;
import com.kirisaki.marker.generator.file.DynamciFileGenerator;
import com.kirisaki.marker.generator.file.StaticFileGenerator;
import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 生成代码生成器的标准方法
 * @author Kirisaki
 */
public abstract class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
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
        //复制原始文件
        String sourceCopyDestPath = copySource(meta, outputPath);
        //代码生成
        generateCode(meta, outputPath);

        //构建jar包
        JarGenerator.doGenerate(outputPath);
        //封装脚本
        String jarPath = buildScript(meta, outputPath);

        //生成.gitignore文件
            StaticFileGenerator.copyFilesByHutool(projectPath+ File.separator+".gitignore",outputPath);
        //初始化git仓库
        GitGenerator.gitInit(outputPath);

        //生成精简的文件jar包
        buildDist(outputPath, sourceCopyDestPath, jarPath);

    }

    /**
     * 生成精简的运行程序
     * @param outputPath 输出文件路径
     * @param sourceCopyDestPath 源复制文件路径
     * @param jarPath jar包路径
     */
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath) {
        String disOutputPath = outputPath +"-dis";
        String jarInputPath = outputPath + File.separator + jarPath;
        String jarAbsolutePath = disOutputPath + File.separator;
        //生成target目录
        FileUtil.mkdir(jarAbsolutePath);
        //生成jar包
        String disOutputJarPath = jarAbsolutePath+ File.separator+ jarPath;
        FileUtil.copy(jarInputPath, disOutputJarPath, true);
        //生成精简版的模板文件
        FileUtil.copy(sourceCopyDestPath, disOutputPath, true);
        //生成精简版的脚本文件
        String scriptPath = outputPath + File.separator + "generator.bat";
        String scriptShPath = outputPath + File.separator + "generator.sh";
        FileUtil.copy(scriptPath, disOutputPath, true);
        FileUtil.copy(scriptShPath, disOutputPath, true);
        return disOutputPath;
    }

    /**
     * 封装脚本
     * @param meta 元信息模型
     * @param outputPath 输出目录
     * @return jar包目录
     */
    protected String buildScript(Meta meta, String outputPath) {
        //生成Script脚本
        String shellOutputFilePath = outputPath + File.separator + "generator";
        //可以使用String,format进行拼接
        String jarPath = "target/" + meta.getName() + "-" + meta.getVersion() + "-jar-with-dependencies.jar";
        ScriptGenerator.doGenerate(shellOutputFilePath,jarPath);
        return jarPath;
    }

    /**
     * 代码生成
     * @param meta 元信息模型
     * @param outputPath 输出目录
     * @throws IOException io异常
     * @throws TemplateException 生成文件异常
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
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

        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //生成命令行入口文件
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
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  StaticFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outFilePath = outputBaseJavaPackagePath + File.separator + "/generator/StaticGenerator.java";
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);

        //获取模板文件路径  pom
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outFilePath = outputPath + File.separator + "pom.xml";
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);
        //生成READ.md文件
        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
        outFilePath = outputPath + File.separator + "README.md";
        DynamciFileGenerator.dynamic(inputFilePath, outFilePath, meta);
    }

    /**
     * 复制原始文件
     * @param meta 元信息
     * @param outputPath 输出目录
     * @return 源文件复制后的目录
     */
    protected String copySource(Meta meta, String outputPath) {
        //将模板文件生成到指定相对路径
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, true);
        return sourceCopyDestPath;
    }

    /**
     * 生成压缩包
     * @param outputPath 精简版目录
     * @return 生成压缩包的地址
     */
    protected String buildZip(String outputPath){
        String zipPath = outputPath + ".zip";
        ZipUtil.zip(outputPath, zipPath);
        return zipPath;
    }
}

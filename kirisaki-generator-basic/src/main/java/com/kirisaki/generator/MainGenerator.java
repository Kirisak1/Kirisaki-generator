package com.kirisaki.generator;

import com.kirisaki.model.MainTemplate;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void doGenerator(Object data) throws TemplateException, IOException {

        //生成动态文件
        String inputRootPath = "D:\\develop\\ideaprojects\\kirisaki-generator\\kisirsaki-generator-demo-project\\acm-template-pro";
        String outputRootPath = "D:\\develop\\ideaprojects\\kirisaki-generator\\acm-template-pro";
        String input;
        String output;
        input = new File(inputRootPath, "/src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        output = new File(outputRootPath, "/src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicGenerator.dynamic(input, output, data);
        //生成静态文件
        input = new File(inputRootPath, ".gitignore").getAbsolutePath();
        output = new File(outputRootPath, ".gitignore").getAbsolutePath();
        StaticGenerator.copyFilesByRecursive(input, output);
        //生成静态文件
        input = new File(inputRootPath, "README.md").getAbsolutePath();
        output = new File(outputRootPath, "README.md").getAbsolutePath();
        StaticGenerator.copyFilesByRecursive(input, output);

    }

    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplate configTemplate = new MainTemplate();
        configTemplate.setAuthor("KK");
        configTemplate.setSummary("KK求和");
        configTemplate.setFlag(false);
        doGenerator(configTemplate);
    }
}

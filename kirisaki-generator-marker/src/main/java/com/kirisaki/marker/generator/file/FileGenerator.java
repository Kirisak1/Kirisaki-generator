package com.kirisaki.marker.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author Kirisaki
 */
public class FileGenerator {
    public static void doGenerator(Object data) throws TemplateException, IOException {
        //获取当前文件夹的路径
        String property = System.getProperty("user.dir");
        //获取当前文件夹的父文件夹
        File parent = new File(property).getParentFile();
        //获取输入文件夹
        String inptut = parent+ File.separator+"kisirsaki-generator-demo-project/acm-template";
        //复制文件
        StaticFileGenerator.copyFilesByHutool(inptut,property);
        //获取输出文件夹
        String dynamicInput = property + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        //获取输出文件
        String dynamicOut = property + File.separator+"/acm-template/src/com/yupi/acm/MainTemplate.java";
        //生成动态文件
        DynamciFileGenerator.dynamic(dynamicInput,dynamicOut,data);
    }
}

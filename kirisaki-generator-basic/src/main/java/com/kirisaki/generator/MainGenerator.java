package com.kirisaki.generator;

import com.kirisaki.model.ConfigTemplate;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void doGenerator(Object data) throws TemplateException, IOException {
        //生成静态文件
        String property = System.getProperty("user.dir");
        File parent = new File(property).getParentFile();
        String inptut = parent+ File.separator+"kisirsaki-generator.bat-demo-project/acm-template";
        StaticGenerator.copyFilesByRecursive(inptut,property);
        //生成动态文件
        String dynamicInput = property + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOut = property + File.separator+"/acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicGenerator.dynamic(dynamicInput,dynamicOut,data);
        
    }
    public static void main(String[] args) throws TemplateException, IOException {
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.setAuthor("KK");
        configTemplate.setSummary("KK求和");
        configTemplate.setFlag(false);
        doGenerator(configTemplate);
    }
}

package com.kirisaki.generator;

import com.kirisaki.model.ConfigTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 动态生成
 * @author Kirisaki
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String property = System.getProperty("user.dir");
        String input = new File(property) + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String out = new File(property)+File.separator+"MainTemplate.java";
        //数据
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.setAuthor("kirisaki");
        configTemplate.setFlag(false);
        configTemplate.setSummary("求和结果");
        dynamic(input,out,configTemplate);
    }

    /**
     * 动态生成Java文件
     * @param input   需要生成的模板的相对路径
     * @param out     需要生成的文件名以及地址
     * @param data    所需要的数据
     * @throws IOException
     * @throws TemplateException
     */
    public static void dynamic(String input , String out ,Object data) throws IOException, TemplateException {
        //添加freemarker的配置
        File parent = new File(input).getParentFile();
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(parent);
        cfg.setDefaultEncoding("utf-8");
        cfg.setNumberFormat("0.######");
        String outName = new File(input).getName();
        Template template = cfg.getTemplate(outName);

        //生成文件
        FileWriter writer = new FileWriter(out);
        template.process(data, writer);
        writer.close();
    }

}

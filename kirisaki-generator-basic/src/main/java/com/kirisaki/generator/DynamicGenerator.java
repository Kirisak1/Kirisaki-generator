package com.kirisaki.generator;

import cn.hutool.core.io.FileUtil;
import com.kirisaki.model.MainTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        MainTemplate configTemplate = new MainTemplate();
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
        //修复生成文件中的乱码问题
        Template template = cfg.getTemplate(outName);
        if (!FileUtil.exist(out)) {
            FileUtil.touch(out);
        }
        //生成文件
        // FileWriter writer = new FileWriter(out);
        //修改输出的java文件乱码问题
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(out)), StandardCharsets.UTF_8));
        template.process(data, writer);
        writer.close();
    }

}

package com.kirisaki.marker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author Kirisaki
 * 测试调用
 */
public class MainGenerator extends  GenerateTemplate {
    /**
     * 通过模板文件模式来定制化相关代码
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     */
    @Override
    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath) {
       super.buildDist(outputPath, sourceCopyDestPath, jarPath);
    }

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        new MainGenerator().doGenerate();

    }
}

package com.kirisaki.marker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author Kirisaki
 * 生成代码生成器压缩包
 */
public class ZipGenerator extends  GenerateTemplate {
    /**
     * 通过模板文件模式来定制化相关代码
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     */
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath) {
        String distPath = super.buildDist(outputPath, sourceCopyDestPath, jarPath);
        return  super.buildZip(distPath);
    }
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        new ZipGenerator().doGenerate();
    }
}

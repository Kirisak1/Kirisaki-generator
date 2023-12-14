package com.kirisaki.marker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kirisaki.marker.meta.Meta.FileConfig;
import com.kirisaki.marker.meta.Meta.ModelConfig;

import java.nio.file.Paths;
import java.util.List;

/**
 * 对元数据进行校验
 *
 * @author Kirisaki
 */
public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        //不需要再new 了 可以直接传进来一个meta对象
        String author = meta.getAuthor();
        String name = meta.getName();
        String basePackage = meta.getBasePackage();
        String version = meta.getVersion();
        String description = meta.getDescription();
        String createTime = meta.getCreateTime();
        FileConfig fileConfig = meta.getFileConfig();
        ModelConfig modelConfig = meta.getModelConfig();
        //校验
        if (StrUtil.isEmpty(author)) {
            meta.setAuthor("kk");
        }
        if (StrUtil.isBlank(name)) {
            meta.setName("my-generator");
        }
        if (StrUtil.isBlank(basePackage)) {
            meta.setBasePackage("com.kirisaki");
        }
        if (StrUtil.isBlank(version)) {
            meta.setVersion("1.0.0");
        }
        if (StrUtil.isBlank(description)) {
            meta.setDescription("我的代码生成器");
        }
        if (StrUtil.isBlank(createTime)) {
            meta.setCreateTime(DateUtil.today());
        }
        if (ObjectUtil.isEmpty(fileConfig)) {
            throw new MetaException("this fileConfig is null");
        }
        //校验
        String sourceRootPath = fileConfig.getSourceRootPath();
        String inputRootPath = fileConfig.getInputRootPath();
        String outputRootPath = fileConfig.getOutputRootPath();
        String type = fileConfig.getType();
        List<FileConfig.FileInfo> files = fileConfig.getFiles();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("this sourceRootPath is null");
        }
        if (StrUtil.isBlank(inputRootPath)) {
            fileConfig.setInputRootPath(".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString());
        }
        if (StrUtil.isBlank(outputRootPath)) {
            fileConfig.setOutputRootPath("generated");
        }
        if (StrUtil.isBlank(type)) {
            fileConfig.setType("dir");
        }
        if (CollectionUtil.isEmpty(files)) {
            throw new MetaException("生成文件为空,请传入文件");
        }

        for (FileConfig.FileInfo file : files) {
            String inputPath = file.getInputPath();
            String outputPath = file.getOutputPath();
            String fileType = file.getType();
            String generateType = file.getGenerateType();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("inputPath is null");
            }
            if (StrUtil.isBlank(outputPath)) {
                if (".ftl".equals(FileUtil.getSuffix(inputPath))) {
                    file.setOutputPath(StrUtil.sub(inputPath, 0, FileUtil.lastIndexOfSeparator(".ftl")));
                } else {
                    file.setOutputPath(inputPath);
                }
            }
            if (StrUtil.isBlank(fileType)) {
                //判断末尾是否有后缀
                if (StrUtil.isNotBlank(FileUtil.getSuffix(inputPath))) {
                    file.setType("file");
                } else {
                    file.setType("dir");
                }
            }

            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    file.setGenerateType("dynamic");
                } else {
                    file.setGenerateType("static");
                }
            }
        }
        if (ObjectUtil.isEmpty(modelConfig)) {
            throw new MetaException("modelConfig is null");
        }
        List<ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollectionUtil.isEmpty(models)) {
            throw new MetaException("models is null");
        }
        for (ModelConfig.ModelInfo model : models) {

            if (StrUtil.isBlank(model.getFieldName())) {
                throw new MetaException("model fieldName is null");
            }
            if (StrUtil.isBlank(model.getType())) {
                model.setType("String");
            }
        }
    }
}

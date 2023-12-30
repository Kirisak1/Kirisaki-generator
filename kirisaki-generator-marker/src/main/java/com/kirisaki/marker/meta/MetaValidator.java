package com.kirisaki.marker.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kirisaki.marker.meta.Meta.FileConfig;
import com.kirisaki.marker.meta.Meta.ModelConfig;
import com.kirisaki.marker.meta.enums.FileGenerateEnum;
import com.kirisaki.marker.meta.enums.FileTypeEnum;
import com.kirisaki.marker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对元数据进行校验
 *
 * @author Kirisaki
 */
public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        //不需要再new 了 可以直接传进来一个meta对象

        //基本信息校验
        validAndFillMetaRoot(meta);
        //生成文件校验
        validAndFillFileConfigValid(meta);
        //命令行参数校验
        validAndFIllModelConfigValid(meta);
    }

    /**
     * 命令行参数校验
     *
     * @param meta
     */
    private static void validAndFIllModelConfigValid(Meta meta) {
        ModelConfig modelConfig = meta.getModelConfig();
        if (ObjectUtil.isEmpty(modelConfig)) {
            // throw new MetaException("modelConfig is null");
            return;
        }
        List<ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollUtil.isEmpty(models)) {
            throw new MetaException("models is null");
        }
        for (ModelConfig.ModelInfo model : models) {
            if (StrUtil.isNotEmpty(model.getGroupKey())) {
                String allArgsStr = model.getModels().stream()
                        .map(subModel -> String.format("\"--%s\"", subModel.getFieldName())
                        )
                        .collect(Collectors.joining(","));
                model.setAllArgsStr(allArgsStr);
                continue;
            }

            if (StrUtil.isBlank(model.getFieldName())) {
                throw new MetaException("model fieldName is null");
            }
            String type = model.getType();
            type =  StrUtil.blankToDefault(type, ModelTypeEnum.STRING.getValue());
            model.setType(type);
        }
    }

    /**
     * 生成文件校验
     *
     * @param meta
     */
    private static void validAndFillFileConfigValid(Meta meta) {
        FileConfig fileConfig = meta.getFileConfig();
        if (ObjectUtil.isEmpty(fileConfig)) {
            // throw new MetaException("this fileConfig is null");  是return 还是抛异常有待商榷
            return;
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

        outputRootPath = StrUtil.blankToDefault(outputRootPath, "generated");
        fileConfig.setOutputRootPath(outputRootPath);

        type = StrUtil.blankToDefault(type, FileTypeEnum.DIR.getValue());
        fileConfig.setType(type);

        if (CollectionUtil.isEmpty(files)) {
            throw new MetaException("生成文件为空,请传入文件");
        }

        for (FileConfig.FileInfo file : files) {
            String inputPath = file.getInputPath();
            String outputPath = file.getOutputPath();
            String fileType = file.getType();
            String generateType = file.getGenerateType();
            //todo 如果分组就不做校验?
            if (FileTypeEnum.GROUP.getValue().equals(fileType)) {
                continue;
            }
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
                    file.setType(FileTypeEnum.FILE.getValue());
                } else {
                    file.setType(FileTypeEnum.DIR.getValue());
                }
            }

            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    file.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());
                } else {
                    file.setGenerateType(FileGenerateEnum.STATIC.getValue());
                }
            }
        }
    }

    /**
     * 基本信息校验
     *
     * @param meta
     */
    private static void validAndFillMetaRoot(Meta meta) {

        String author = meta.getAuthor();
        String name = meta.getName();
        String basePackage = meta.getBasePackage();
        String version = meta.getVersion();
        String description = meta.getDescription();
        String createTime = meta.getCreateTime();
        //校验

        author = StrUtil.emptyToDefault(author, "kk");
        meta.setAuthor(author);

        name = StrUtil.blankToDefault(name, "my-generator");
        meta.setName(name);

        basePackage = StrUtil.blankToDefault(basePackage, "com.kirisaki");
        meta.setBasePackage(basePackage);

        version = StrUtil.blankToDefault(version, "1.0.0");
        meta.setVersion(version);

        description = StrUtil.blankToDefault(description, "我的代码生成器");
        meta.setDescription(description);

        createTime = StrUtil.blankToDefault(createTime, DateUtil.today());
        meta.setCreateTime(createTime);
    }
}

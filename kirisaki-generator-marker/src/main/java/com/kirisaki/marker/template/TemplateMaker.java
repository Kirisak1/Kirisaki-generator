package com.kirisaki.marker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.enums.FileGenerateEnum;
import com.kirisaki.marker.meta.enums.FileTypeEnum;
import com.kirisaki.marker.meta.enums.ModelTypeEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板制作类
 */
public class TemplateMaker {
    public static void main(String[] args) {
        //一 输入基本信息
        //1. 输入项目基本信息
        String description = "kk代码生成器";
        String name = "kirisaki-generate";
        //2.输入文件信息
        //该方法在win系统下得到的路径有问题
        String projectPath = System.getProperty("user.dir");
        String sourceRootPath = new File(projectPath).getParent() + File.separator + "kisirsaki-generator-demo-project/acm-template";
        //修改路径
        sourceRootPath = StrUtil.replace(sourceRootPath, "\\", "/");

        String fileInputPath = "src/com/yupi/acm/MainTemplate.java";
        String fileOutputPath = fileInputPath + ".ftl";
        // 3.输入模板参数信息
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType(ModelTypeEnum.STRING.getValue());
        modelInfo.setDefaultValue("sum:");

        //二 使用字符串替换生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        String fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        //使用占位符来替换  String. format() API的使用   , 避免使用魔法值, 能够引用的地方一定要引用
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        //使用hutool封装好的API, 来减少对某些情况的判断
        String newFileContent = StrUtil.replace(fileContent, "Sum: ", replacement);
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        //生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        //1.构造配置参数
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);

        Meta.FileConfig fileConfig = new Meta.FileConfig();
        meta.setFileConfig(fileConfig);
        fileConfig.setSourceRootPath(sourceRootPath);
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        fileConfig.setFiles(fileInfoList);
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());
        fileInfoList.add(fileInfo);


        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
        meta.setModelConfig(modelConfig);
        List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        modelConfig.setModels(modelInfoList);
        modelInfoList.add(modelInfo);
        //toJsonPrettyStr 转换成格式化后的json字符串  toJsonStr 不会格式化
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
    }
}

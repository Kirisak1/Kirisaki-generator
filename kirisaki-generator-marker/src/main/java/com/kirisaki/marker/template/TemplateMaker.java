package com.kirisaki.marker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.enums.FileGenerateEnum;
import com.kirisaki.marker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板制作类
 */
public class TemplateMaker {
    public static void main(String[] args) {
        //封装前端传参
        Meta meta = new Meta();
        meta.setName("kirisaki-generate");
        meta.setDescription("kk代码生成器");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "kisirsaki-generator-demo-project/acm-template";
        String inputFilePath = "src/com/yupi/acm/MainTemplate.java";

        //模型参数(首次)
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("sum =");

        // 模型参数(第二次)
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        //替换变量(首次)
        // String searchStr = "Sum:";
        //替换变量(第二次)
        String searchStr = "MainTemplate";
        long id = makeTemplate(meta, originProjectPath, inputFilePath, modelInfo, searchStr, 1738565126300737536L);
        System.out.println(id);
    }

    /**
     * 生成模板
     * @param id 模板的包名
     * @return 返回生成模板的包名
     */
    public static long makeTemplate( Meta newMeta, String originProjectPath, String inputFilePath, Meta.ModelConfig.ModelInfo modelInfo,String searchStr,Long id) {

        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        //二 工作空间隔离  将原始目录复制到指定目录
        String projectPath = System.getProperty("user.dir");
        //根据id来隔离不同批次生成的文件
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;

        //是否为首次制作模板
        if (!FileUtil.exist(new File(templatePath))) {
            FileUtil.mkdir(new File(templatePath));
            //如果不存在才复制
            FileUtil.copy(new File(originProjectPath), new File(templatePath), true);
        }

        //一 输入基本信息
        // 输入文件信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        //该方法在win系统下得到的路径有问题
        sourceRootPath = StrUtil.replace(sourceRootPath, "\\", "/");
        String fileInputPath = inputFilePath;
        String fileOutputPath = fileInputPath + ".ftl";
        //二 使用字符串替换生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        //如果非首次制作就在ftl的基础上继续修改
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        //使用占位符来替换  String. format() API的使用   , 避免使用魔法值, 能够引用的地方一定要引用
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        //使用hutool封装好的API, 来减少对某些情况的判断
        String newFileContent = StrUtil.replace(fileContent, searchStr, replacement);
        //输出ftl模板文件
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());
        //生成配置文件  json字符串
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        //非首次制作  在mate.json模板的基础上继续添加
        if (FileUtil.exist(metaOutputPath)) {
            //通过fileutils将文件中的内容读取出来
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            //如果非首次制作就将新对象赋值给旧对象, 同时保留新对象的值  同时忽略空值
            BeanUtil.copyProperties(newMeta,oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.add(fileInfo);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);
            //去重
            newMeta.getFileConfig().setFiles(distinctFile(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModel(modelInfoList));

        } else {

            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.add(fileInfo);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);
        }
        //更新元信息 toJsonPrettyStr 转换成格式化后的json字符串  toJsonStr 不会格式化
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);

        return id;
    }

    /**
     * 文件去重
     *
     * @param fileInfoList 非首次挖坑的文件
     * @return 返回去重后的文件列表
     */
    private static List<Meta.FileConfig.FileInfo> distinctFile(List<Meta.FileConfig.FileInfo> fileInfoList) {
        return new ArrayList<>(fileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, fileinfo -> fileinfo, (o1, o2) -> o2)
                )
                .values());
    }

    /**
     * 模板参数去重
     *
     * @param modelInfoList 模板参数列表
     * @return 去重后的模板参数
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModel(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        return new ArrayList<>(modelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, modelinfo -> modelinfo, (o1, o2) -> o2)
                )
                .values());
    }
}

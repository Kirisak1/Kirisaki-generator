package com.kirisaki.marker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.enums.FileGenerateEnum;
import com.kirisaki.marker.meta.enums.FileTypeEnum;
import com.kirisaki.marker.template.enums.FileFileterRangeEnum;
import com.kirisaki.marker.template.enums.FileFilterRuleEnum;
import com.kirisaki.marker.template.model.FileFilterConfig;
import com.kirisaki.marker.template.model.TemplateMakerFileConfig;
import com.kirisaki.marker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
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
        String originProjectPath = new File(projectPath).getParent() + File.separator + "kisirsaki-generator-demo-project/springboot-init";
        // String inputFilePath = "src/main/java/com/yupi/springbootinit";

        //模型参数(首次)
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("sum =");

        //替换变量(首次)

        //替换变量(第二次)
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
        String inputFilePath2 = "src/main/resources/application.yml";

        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("test");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        //模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("test");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();


        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFileterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFiles(fileFilterConfigList);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));

        //分组测试代码
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig,templateMakerModelConfig, 1738803991506710528L);
        System.out.println(id);
    }

    /**
     * 生成模板
     *
     * @param id 模板的包名
     * @return 返回生成模板的包名
     */
    public static long makeTemplate(Meta newMeta,
                                    String originProjectPath,
                                    TemplateMakerFileConfig templateMakerFileConfig,
                                    TemplateMakerModelConfig templateMakerModelConfig,
                                    Long id) {
        //处理model封装对象
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream()
                .map(modelInfoConfig -> {
                    Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
                    BeanUtil.copyProperties(modelInfoConfig, modelInfo);
                    return modelInfo;
                })
                .collect(Collectors.toList());
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        //如果是模型分组
        if (modelGroupConfig != null) {
            String condition = modelGroupConfig.getCondition();
            String groupKey = modelGroupConfig.getGroupKey();
            String groupName = modelGroupConfig.getGroupName();

            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();

            groupModelInfo.setGroupKey(groupKey);
            groupModelInfo.setGroupName(groupName);
            groupModelInfo.setModels(inputModelInfoList);
            groupModelInfo.setCondition(condition);

            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        }else {
            newModelInfoList.addAll(inputModelInfoList);
        }


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

        ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        //增加使用过滤器
        for (TemplateMakerFileConfig.FileInfoConfig fileinfoConfig : templateMakerFileConfig.getFiles()) {
            String inputFilePath = fileinfoConfig.getPath();
            //如果传过来的是相对路径就转换成绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)) {
                //二 使用字符串替换生成模板文件
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

            //文件过滤
            List<FileFilterConfig> fileinfoConfigsFiles = fileinfoConfig.getFiles();
            List<File> files = FileFilter.doFilter(fileinfoConfigsFiles, inputFilePath);
            for (File file : files) {
                //生成单个文件
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        //如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            //新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            //文件全放在一个组内
            groupFileInfo.setFiles(newFileInfoList);
            //改变这里之后下面的代码就可以不用改变了
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

        //生成配置文件  json字符串
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        //非首次制作  在mate.json模板的基础上继续添加
        if (FileUtil.exist(metaOutputPath)) {
            //通过fileutils将文件中的内容读取出来
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            //如果非首次制作就将新对象赋值给旧对象, 同时保留新对象的值  同时忽略空值
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);
            //去重
            newMeta.getFileConfig().setFiles(distinctFile(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModel(modelInfoList));

        } else {

            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);
        }
        //更新元信息 toJsonPrettyStr 转换成格式化后的json字符串  toJsonStr 不会格式化
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);

        return id;
    }

    /**
     * 生成单个文件模板的方法
     *
     * @param templateMakerModelConfig      制作模板配置参数
     * @param sourceRootPath 目标源文件目录
     * @param inputFile      需要生成的模板文件
     * @return 返回生成的fileInfo信息
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile) {

        String fileInputAbsolutePath = inputFile.getAbsolutePath().replace("\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";

        //如果非首次制作就在ftl的基础上继续修改
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        //支持多个模板:对于同一个文件的内容,遍历模板进行多轮替换

        String newFileContent = fileContent;
        String replacement;
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();

            for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : models) {
                //使用占位符来替换  String. format() API的使用   , 避免使用魔法值, 能够引用的地方一定要引用
                if (modelGroupConfig != null) {
                    replacement = String.format("${%s.%s}",modelGroupConfig.getGroupKey() ,modelInfoConfig.getFieldName());
                }else {
                    replacement = String.format("${%s}", modelInfoConfig.getFieldName());
                }
                String searchStr = modelInfoConfig.getReplaceText();
                //使用hutool封装好的API, 来减少对某些情况的判断
                newFileContent = StrUtil.replace(newFileContent, searchStr, replacement);

            }




        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());

        //判断是否有需要替换的内容
        if (newFileContent.equals(fileContent)) {
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateEnum.STATIC.getValue());
        } else {
            fileInfo.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());
            //输出ftl模板文件   只有需要生成模板文件的才需要写入, 不需要的就可以不写
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    /**
     * 文件去重
     *
     * @param fileInfoList 非首次挖坑的文件
     * @return 返回去重后的文件列表
     */
    private static List<Meta.FileConfig.FileInfo> distinctFile(List<Meta.FileConfig.FileInfo> fileInfoList) {
        //分组策略:同分组内文件 merge,不同分组保留

        //有分组的
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));
        //同组内配置合并
        //保留每个组对应的合并后的对象map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)).values());
            //使用新group配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());
        //无分组
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                )
                .values()));
        return resultList;
    }

    /**
     * 模板参数去重
     *
     * @param modelInfoList 模板参数列表
     * @return 去重后的模板参数
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModel(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        //增加分组后的模型去重
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));
        //分组配置
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            //某个分组下的model集合
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            ArrayList<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values());

            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            groupKeyMergedModelInfoMap.put(entry.getKey(), newModelInfo);
        }
        // 合并分组的配置集合
        ArrayList<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());
        //合并不分组的配置集合
        resultList.addAll(new ArrayList<>(modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values()));

        return resultList;
    }

}

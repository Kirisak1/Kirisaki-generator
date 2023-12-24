package com.kirisaki.marker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.kirisaki.marker.template.enums.FileFileterRangeEnum;
import com.kirisaki.marker.template.enums.FileFilterRuleEnum;
import com.kirisaki.marker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件过滤
 */
public class FileFilter {
    /**
     * 对某个文件或者目录进行过滤,返回文件列表
     *
     * @param fileFilterConfigList
     * @param filePath
     * @return
     */
    public static List<File> doFilter(List<FileFilterConfig> fileFilterConfigList, String filePath) {
        //根据路径获取所有文件
        List<File> files = FileUtil.loopFiles(filePath);
        return files.stream().filter(file ->
                doSingleFileFilter(fileFilterConfigList, file)
        ).collect(Collectors.toList());

    }

    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigList 过滤规则
     * @param file                 单个文件
     * @return 是否保留
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        //所有过滤器执行结束后的结果
        boolean result = true;

        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFileterRangeEnum fileFileterRangeEnum = FileFileterRangeEnum.getEnumByValue(range);
            if (fileFileterRangeEnum == null) {
                continue;
            }

            //要过滤的原内容
            String content = fileName;

            switch (fileFileterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }
            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
            //全部判断那之后再返回, 不可以只判断那一次就返回
            if (!result) {
                return false;
            }
        }
        return true;
    }
}



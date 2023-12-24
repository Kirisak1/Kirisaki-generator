package com.kirisaki.marker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模板文件配置类
 */
@Data
public class TemplateMakerFileConfig {
    private List<FileInfoConfig> files;
    //分组
    private FileGroupConfig fileGroupConfig;

    /**
     * 文件信息配置类
     */
    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        private String path;
        private List<FileFilterConfig> files;
    }

    /**
     * 文件分组配置类
     */
    @Data
    public static class FileGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;
    }
}

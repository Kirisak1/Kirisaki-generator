package com.kirisaki.marker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模板文件配置类
 */
@Data
public class TemplateMakerFileConfig {
    private List<FileinfoConfig> files;

    @Data
    @NoArgsConstructor
    public static class FileinfoConfig {
        private String path;
        private List<FileFilterConfig> files;
    }
}

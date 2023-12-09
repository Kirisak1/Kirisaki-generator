package com.kirisaki.marker.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kirisaki
 */
@NoArgsConstructor
@Data
public class Meta {
    private String author;
    private String name;
    private String basePackage;
    private String version;
    private String description;
    private String createTime;
    private FileConfig fileConfig;
    private ModelConfig modelConfig;

    @NoArgsConstructor
    @Data
    public static class FileConfig {
        private String inputPath;
        private String outputPath;
        private String type;
        private List<FileInfo> files;

        @NoArgsConstructor
        @Data
        public static class FileInfo{
            private String inputPath;
            private String outputPath;
            private String type;
            private String generateType;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ModelConfig {
        private List<ModelInfo> models;

        @NoArgsConstructor
        @Data
        public static class ModelInfo{
            private String fieldName;
            private String type;
            private String comment;
            private Object defaultValue;
            private String abbr;
        }
    }
}
package com.kirisaki.marker.meta.enums;

/**
 * 文件类型枚举类
 * @author Kirisaki
 */
public enum FileGenerateEnum {
    /**
     * 静态生成的文件
     */
    STATIC("静态文件", "static"),
    /**
     * 动态生成的文件
     */
    DYNAMIC("动态文件", "dynamic");

    private String text;
    private String value;

    FileGenerateEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}

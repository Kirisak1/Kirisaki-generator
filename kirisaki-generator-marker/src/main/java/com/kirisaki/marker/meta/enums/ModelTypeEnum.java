package com.kirisaki.marker.meta.enums;

/**
 * 模型类型枚举类
 *
 * @author Kirisaki
 */
public enum ModelTypeEnum {
    /**
     * 字符串类型
     */
    STRING("字符串", "String"),
    BOOLEAN("布尔类型", "boolean");
    private String text;
    private String value;

    ModelTypeEnum(String text, String value) {
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

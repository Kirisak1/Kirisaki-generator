package com.kirisaki.marker.template.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 过滤规则枚举类
 */
@Getter
public enum FileFilterRuleEnum {
    /**
     * 包含
     */
    CONTAINS("包含", "contains"),
    /**
     * 开始
     */
    STARTS_WITH("前缀匹配", "startsWith"),
    /**
     * 以..结束
     */
    ENDS_WITH("后缀匹配", "endsWith"),
    /**
     * 正则匹配
     */
    REGEX("正则", "regex"),
    /**
     * 相等
     */
    EQUALS("相等", "equals");
    /**
     * 中文概述
     */
    private final String text;
    /**
     * value值
     */
    private final String value;

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value找到对应的枚举类
     *
     * @param value
     * @return
     */
    public static FileFilterRuleEnum getEnumByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (FileFilterRuleEnum anEnum : FileFilterRuleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

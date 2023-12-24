package com.kirisaki.marker.template.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 过滤范围枚举类
 */
@Getter
public enum FileFileterRangeEnum {

    FILE_NAME("文件名称", "fileName"),
    FILE_CONTENT("文件内容", "fileContent");


    private final String text;

    private final String value;

    FileFileterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value值获取枚举对象
     * @param value
     * @return
     */
    public static FileFileterRangeEnum getEnumByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (FileFileterRangeEnum anEnum : FileFileterRangeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

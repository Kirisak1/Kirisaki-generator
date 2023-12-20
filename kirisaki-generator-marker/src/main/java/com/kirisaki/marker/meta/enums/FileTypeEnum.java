package com.kirisaki.marker.meta.enums;

import lombok.Getter;

/**
 * 文件生成枚举值
 *
 * @author Kirisaki
 */

@Getter
public enum FileTypeEnum {

    /**
     * 目录
     */
    DIR("目录", "dir"),
    /**
     * 文件
     */
    FILE("文件", "file"),
    /**
     * 分组
     */
    GROUP("文件组", "group");
    private String text;
    private String value;

    FileTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
}

package com.kirisaki.marker.model;

import lombok.Data;

/**
 * @author Kirisaki
 * 动态文件对象
 */
@Data
public class DataModel {
    /**
     * 作者名称
     */
    private String author ="";
    /**
     * 最后求和输出信息
     */
    private String summary = "";
    /**
     * 是否开启循环
     */
    private Boolean flag ;
}

package com.kirisaki.marker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * 静态文件生成类
 *
 * @author Kirisaki
 */
public class StaticFileGenerator {
    /**
     * 拷贝文件
     * @param input 输入路径
     * @param output 输出路径
     */
    public static void copyFilesByHutool(String input, String output) {
        FileUtil.copy(input, output, false);
    }
}




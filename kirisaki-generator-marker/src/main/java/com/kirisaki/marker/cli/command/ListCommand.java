package com.kirisaki.marker.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.*;

import java.io.File;
import java.util.List;

/**
 * @author kirisaki
 */
@Command(name = "list",description = "输出文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    @Override
    public void run() {
        // 获取当前目录的属性
        String property = System.getProperty("user.dir");
        // 创建文件对象
        File file = new File(property);
        // 获取父目录文件对象
        File parentFile = file.getParentFile();
        // 获取父目录的绝对路径
        String absolutePath = parentFile.getAbsolutePath();
        // 循环遍历指定路径下的所有文件   通过hutool的工具包来循环文件下的所有路径
        List<File> files = FileUtil.loopFiles(absolutePath + "/kisirsaki-generator-demo-project/acm-template");
        // 遍历文件列表并打印每个文件
        for (File file1 : files) {
            System.out.println(file1.getAbsoluteFile());
        }
    }


}

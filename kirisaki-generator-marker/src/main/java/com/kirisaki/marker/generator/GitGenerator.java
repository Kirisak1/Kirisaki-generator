package com.kirisaki.marker.generator;

import cn.hutool.core.util.StrUtil;

import java.io.*;

/**
 * 初始化git本地库
 *
 * @author Kirisaki
 */
public class GitGenerator {
    private GitGenerator() {

    }
    public static void gitInit(String initPath) throws IOException, InterruptedException {
        //创建命令
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("git init");
        //构建process储存命令
        ProcessBuilder processBuilder = new ProcessBuilder(stringBuilder.toString().split(" "));
        //命令执行的目录
        processBuilder.directory(new File(initPath));
        //执行命令
        Process process = processBuilder.start();
        //监听命令
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (StrUtil.isBlank(reader.readLine())) {
            System.out.println(reader.lines());
        }
        inputStream.close();
        int exitCode = process.waitFor();
        System.out.println("初始化git仓库退出码:" + exitCode);
    }
}

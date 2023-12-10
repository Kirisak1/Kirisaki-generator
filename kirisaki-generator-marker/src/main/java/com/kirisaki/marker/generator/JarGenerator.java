package com.kirisaki.marker.generator;

import java.io.*;

/**
 * @author Kirisaki
 */
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //mvn命令
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String outMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = winMavenCommand;
        //拆分命令 , 必须拆分
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        //读取命令输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        //等待命令执行结束
        int exitCode = process.waitFor();
        System.out.println("命令执行结束,退出码:" + exitCode);
    }
}

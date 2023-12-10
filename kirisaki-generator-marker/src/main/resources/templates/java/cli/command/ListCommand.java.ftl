package ${basePackage}.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.*;

import java.io.File;
import java.util.List;

/**
 * @author ${author}
 */
@Command(name = "list",description = "输出文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    @Override
    public void run() {
        String inputPath = "${fileConfig.inputRootPath}";
        // 循环遍历指定路径下的所有文件   通过hutool的工具包来循环文件下的所有路径
        List<File> files = FileUtil.loopFiles(inputPath);
        // 遍历文件列表并打印每个文件
        for (File file1 : files) {
            System.out.println(file1.getAbsoluteFile());
        }
    }


}

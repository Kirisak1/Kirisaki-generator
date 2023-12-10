package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

/**
 * 静态文件生成类
 *
 * @author ${author}
 */
public class StaticGenerator {
    /**
     * 拷贝文件
     * @param input 输入路径
     * @param output 输出路径
     */
    public static void copyFilesByHutool(String input, String output) {
        FileUtil.copy(input, output, false);
    }
}




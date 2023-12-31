package ${basePackage}.cli;

import ${basePackage}.cli.command.ConfigCommand;
import ${basePackage}.cli.command.GenerateCommand;
import ${basePackage}.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * 主类
 *
 * @author ${author}
 */
@Command(name = "${name}", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {

    private final CommandLine commandLine;

    // 定义一个final变量, 防止被修改.
    //通过代码块的形式导入子命令
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand())
                .addSubcommand(new GenerateCommand());

    }

    @Override
    public void run() {
        System.out.println("请输入具体命令或 --help查看帮助手册");
    }

    public Integer doExecute(String[] args) {
        return commandLine.execute(args);

    }
}

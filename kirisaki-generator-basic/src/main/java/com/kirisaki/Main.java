package com.kirisaki;

import com.kirisaki.cli.CommandExecutor;

public class Main {
    public static void main(String[] args) {
        //这里作为调用的接口,为什么不直接在那个类里做调用接口?
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}

package com.kirisaki.cli.pattern;

/**
 * 中介
 *
 * @author kirisaki
 */
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

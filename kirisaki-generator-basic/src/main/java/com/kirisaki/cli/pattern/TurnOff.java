package com.kirisaki.cli.pattern;

/**
 * 具体的命令
 * @author kirisaki
 */
public class TurnOff implements Command {
    private Device device;

    public TurnOff(Device device) {
        this.device = device;
    }
    @Override
    public void execute() {
        device.turnOff();
    }
}

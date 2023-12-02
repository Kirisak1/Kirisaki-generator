package com.kirisaki.cli.pattern;

/**
 * 设备打开具体命令
 * @author kirisaki
 */
public class TurnOn implements Command {
    private Device device;

    public TurnOn(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}

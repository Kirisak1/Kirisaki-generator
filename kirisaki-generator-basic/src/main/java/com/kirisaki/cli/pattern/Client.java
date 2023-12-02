package com.kirisaki.cli.pattern;

/**
 * 客户端 请求调用者
 * @author kirisaki
 */
public class Client {
    public static void main(String[] args) {
        Device tv = new Device("Tv");
        Device studo = new Device("Studo");
        RemoteControl remoteControl = new RemoteControl();
        TurnOff turnOff = new TurnOff(tv);
        remoteControl.setCommand(turnOff);
        remoteControl.pressButton();
        TurnOn turnOn = new TurnOn(studo);
        remoteControl.setCommand(turnOn);
        remoteControl.pressButton();
    }
}

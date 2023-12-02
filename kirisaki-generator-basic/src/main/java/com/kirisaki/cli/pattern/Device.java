package com.kirisaki.cli.pattern;

/**
 * 服务端
 *
 * @author kirisaki
 */
public class Device {
    private String name;

    public Device(String name) {
        this.name = name;
    }
    public void  turnOn() {
        System.out.println(this.name + "设备开启了");
    }
    public void turnOff() {
        System.out.println(this.name + "设备关闭了");
    }
}

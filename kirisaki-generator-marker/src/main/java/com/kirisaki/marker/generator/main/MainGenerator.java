package com.kirisaki.marker.generator.main;

import com.kirisaki.marker.meta.Meta;
import com.kirisaki.marker.meta.MetaManager;

public class MainGenerator {
    public static void main(String[] args) {
        Meta meta = MetaManager.getMeta();
        System.out.println(meta);
    }
}

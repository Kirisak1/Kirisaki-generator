package com.kirisaki.marker.meta;

/**
 * Meta异常类
 *
 * @author Kirisaki
 */
public class MetaException extends RuntimeException {

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.miyatu.mirror.util;

/**
 * Created by yhd on 2019/11/21.
 * Email 1443160259@qq.com
 */
public class MessageWrap {
    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
}

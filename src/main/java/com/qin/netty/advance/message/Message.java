package com.qin.netty.advance.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message抽象基类
 *
 * @author DELL
 * @date 2021/08/15 16:34.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 5834931960863326729L;

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sequenceId=" + sequenceId +
                ", messageType=" + messageType +
                '}';
    }
}

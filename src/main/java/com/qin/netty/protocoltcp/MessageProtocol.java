package com.qin.netty.protocoltcp;

import java.util.Arrays;

/**
 * description
 *
 * @author DELL
 * @date 2021/06/22 22:32.
 */
public class MessageProtocol {

    private int length;

    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageProtocol{" +
                "length=" + length +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}

package org.netty.demo.protocol.serializable;

import java.io.Serializable;

public class Message implements Serializable {


    private static final long serialVersionUID = -5296315429304117678L;

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Message(String body) {
        super();
        this.body = body;
    }

    public Message() {
        super();
    }

    @Override
    public String toString() {
        return "Message [body=" + body + "]";
    }
}
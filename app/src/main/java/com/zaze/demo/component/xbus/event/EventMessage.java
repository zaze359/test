package com.zaze.demo.component.xbus.event;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-28 - 17:37
 */
public class EventMessage {

    private String message;

    public EventMessage() {
    }

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}

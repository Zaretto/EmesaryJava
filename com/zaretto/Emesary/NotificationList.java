package com.zaretto.Emesary;

import java.util.Vector;

public class NotificationList
{
    public NotificationList(String queueId) {
        setQueueID(queueId);
        setitems(new Vector<INotification>());
    }

    private String __Id;
    public String getId() {
        return __Id;
    }

    public void setId(String value) {
        __Id = value;
    }

    private String __QueueID;
    public String getQueueID() {
        return __QueueID;
    }

    public void setQueueID(String value) {
        __QueueID = value;
    }

    private Vector<INotification> __items;
    public Vector<INotification> getitems() {
        return __items;
    }

    public void setitems(Vector<INotification> value) {
        __items = value;
    }

    public void add(INotification M) throws Exception {
        getitems().add(M);
    }

    public void remove(INotification M) throws Exception {
        getitems().remove(M);
    }

}



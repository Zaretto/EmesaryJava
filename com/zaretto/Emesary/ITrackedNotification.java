package com.zaretto.Emesary;


public interface ITrackedNotification   extends INotification
{
    String getTrackingId() throws Exception ;

    void setTrackingId(String value) throws Exception ;

}



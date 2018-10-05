package com.zaretto.Emesary;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*---------------------------------------------------------------------------
 *
 *	Title                : EMESARY Queued Notification
 *
 *	File Type            : Implementation File
 *
 *	Description          : Queued Notification base class - using DataObjects.NET for
 *	                     : the persistency.
 *	                     : 
 *	Author               : Richard Harrison (richard@zaretto.com)
 *
 *	Creation Date        : 14 MAR 2011
 *
 *	Version              : $Header: $
 *
 *  Copyright © 2011 Richard Harrison           All Rights Reserved.
 *
 *---------------------------------------------------------------------------*/
public class QueueNotification   implements IQueueNotification, INotification
{
    public static Date addSeconds(Date date, double seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, (int)seconds*1000);
        return cal.getTime();
    }
    private static double DefaultTimeout = 320;
    // seconds
    public QueueNotification(Object Value) throws Exception {
        this.setValue(Value);
        setTimedOut(false);
        setComplete(false);
        setCreatedDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
        setExpiryDate(addSeconds(getCreatedDate(), QueueNotification.DefaultTimeout));
        setWhenNextReadyToSend(getCreatedDate());
    }

    /**
    * queued elements cannot be linked to objects as this is not supported with the
    * DataObjects.NET
    */
    private Object __Value;
    public Object getValue() {
        return __Value;
    }

    public void setValue(Object value) {
        __Value = value;
    }

    /**
    * Message epoch. UTC.
    * All datetimes must be stored as UTC
    */
    private Date __CreatedDate;
    public Date getCreatedDate() {
        return __CreatedDate;
    }

    public void setCreatedDate(Date value) {
        __CreatedDate = value;
    }

    /**
    * The datetime in UTC when this message expires.
    */
    private Date __ExpiryDate;
    public Date getExpiryDate() {
        return __ExpiryDate;
    }

    public void setExpiryDate(Date value) {
        __ExpiryDate = value;
    }

    /**
    * The datetime in UTC when this message will be ready to be sent.
    * If the recipient(s) managing this notification are able to predict reliably when
    * this notification should next be sent out this filed should contain the value.
    * The queue will use this to decide which pending messages are ready to be sent.
    * Default value is the creation date. Do not set this unless you are certain of the date.
    * Can be used to schedule regular events.
    */
    private Date __WhenNextReadyToSend;
    public Date getWhenNextReadyToSend() {
        return __WhenNextReadyToSend;
    }

    public void setWhenNextReadyToSend(Date value) {
        __WhenNextReadyToSend = value;
    }

    /**
    * Used to control the sending of notifications. If this returns false then the Transmitter
    * should not send this notification.
    * 
    *  @return
    */
    public boolean getIsReadyToSend() throws Exception {
        return !getIsTimedOut() && !getComplete() && getWhenNextReadyToSend().before(Calendar.getInstance().getTime());
    }

    private boolean __Complete;
    public boolean getComplete() {
        return __Complete;
    }

    public void setComplete(boolean value) {
        __Complete = value;
    }

    private boolean __TimedOut;
    public boolean getTimedOut() {
        return __TimedOut;
    }

    public void setTimedOut(boolean value) {
        __TimedOut = value;
    }

    /**
    * when this notification has completed the processing recipient must set this to true.
    * the processing recipient is responsible for follow on notifications.
    * a notification can remain as complete until the transmit queue decides to remove it from the queue.
    * there is no requirement that elements are removed immediately upon completion merely that once complete
    * the transmitter should not notify any more elements.
    * The current notification loop may be completed - following the usual convention unless Completed or Abort
    * is returned as the status.
    */
    public boolean getIsComplete() throws Exception {
        return getComplete();
    }

    /**
    * Used to control the timeout. If this notification has timed out - then the processor is entitled
    * to true.
    * 
    *  @return
    */
    public boolean getIsTimedOut() throws Exception {
        return false;
    }

    private IAsyncOperationCompleted  __Completed = null;
    public IAsyncOperationCompleted  getCompleted() {
        return __Completed;
    }

    public void setCompleted(IAsyncOperationCompleted  value) {
        __Completed = value;
    }

}



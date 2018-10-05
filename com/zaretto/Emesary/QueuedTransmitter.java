package com.zaretto.Emesary;

/*---------------------------------------------------------------------------
 *
 *	Title                : EMESARY Queued Transmitter
 *
 *	File Type            : Implementation File
 *
 *	Description          : Queued Transmitter class
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

import java.util.Calendar;
import java.util.Date;

/**
* Interaction logic for App.xaml
*/
public class QueuedTransmitter  extends Transmitter 
{
    //, IReceiver
    public static final int DefaultRetrySeconds = 60;
    static final String QueuedTransmitterQueueID = "QueuedTransmitter-NotificationList";
    private AutoResetEvent messageWaitEvent = new AutoResetEvent(false);
    NotificationList pendingList;
    public QueuedTransmitter(String queueID) {
        this.setqueueID(queueID + QueuedTransmitterQueueID);
        pendingList = new NotificationList(queueID);
        setPendingFrequencyMs(33);
        setPendingSleepMs(200);
    }

    private String __queueID;
    public String getqueueID() {
        return __queueID;
    }

    public void setqueueID(String value) {
        __queueID = value;
    }

    public void createObjects() throws Exception {
        if (pendingList != null)
            return ;
         
        //TODO: Load from persistent store.
        if (pendingList == null)
            pendingList = new NotificationList(getqueueID());
         
    }

    /**
    * Queue notification for processing with lambda after completion to allow for processing based on result.
    * 
    *  @param M 
    *  @param completed 
    *  @return
    */
    public ReceiptStatus NotifyAll(IQueueNotification M, IAsyncOperationCompleted completed) throws Exception {
        if (completed != null)
            M.setCompleted(completed);
        pendingList.add(M instanceof INotification ? (INotification)M : (INotification)null);
        messageWaitEvent.set();
        return ReceiptStatus.OK;
    }
    public ReceiptStatus NotifyAll(IQueueNotification M) throws Exception {
        return NotifyAll(M,null);
    }
    /**
    * Queue notification for processing.
    * 
    *  @param M 
    *  @return
    */
    public ReceiptStatus NotifyAll(INotification M) throws Exception {
        pendingList.add(M);
        messageWaitEvent.set();
        return ReceiptStatus.Pending;
    }

    // Pending is effectively OK.;
    public void processPending() throws Exception {
        if (pendingList.getitems().size() <= 0)
            return ;
         
        for (INotification notification : pendingList.getitems())
        {
            // Iterate through all notifications that are ready to be processed.
            //            foreach (var notification in pendingList.items.Where(n => DateTime.UtcNow >= n.whenNextReadyToSend))
            if (notification instanceof IQueueNotification)
            {
                IQueueNotification qnotification = notification instanceof IQueueNotification ? (IQueueNotification)notification : (IQueueNotification)null;
                //                    if (DateTime.UtcNow <= qnotification.WhenNextReadyToSend)
                if (!qnotification.getIsReadyToSend())
                    continue;
                 
                //bool process_failed = false;
                //bool processed_ok = false;
                //
                // only notify when the notification ready to be sent. This may be used in addition to the whenNextReadyToSend
                // to priver fine grained control over retries.
                if (qnotification.getIsReadyToSend())
                {
//                    System.Diagnostics.Debug.WriteLine("Process pending " + Calendar.getInstance().getTime().ToShortTimeString() + " - " + notification.ToString());
                    ReceiptStatus notify_result = super.NotifyAll(notification);
                    //switch (notify_result)
                    //{
                    //    case ReceiptStatus.Abort:
                    //    case ReceiptStatus.Fail:
                    //        process_failed = true;
                    //        break;
                    //    case ReceiptStatus.OK:
                    //    case ReceiptStatus.Finished:
                    //        processed_ok = true;
                    //        break;
                    //    case ReceiptStatus.Pending:
                    //    case ReceiptStatus.NotProcessed:
                    //    default:
                    //        break;
                    //}
                    if (qnotification.getCompleted() != null)
                        qnotification.getCompleted().OperationCompleted(qnotification, notify_result);
                     
                    if (qnotification.getIsComplete() || qnotification.getIsTimedOut())
                    {
                        pendingList.remove(notification);
                    }
                     
                    if (notify_result == ReceiptStatus.Finished || notify_result == ReceiptStatus.Abort)
                        break;
                     
                }
                else
                {
//                    System.Diagnostics.Debug.WriteLine("Process pending " + Calendar.getInstance().getTime().ToShortTimeString() + " - not ready: " + notification.ToString());
                } 
            }
            else
            {
                //System.Diagnostics.Debug.WriteLine("QueuedTransmitter; Notify non queued message {0}", notification.ToString());
                ReceiptStatus notify_result = super.NotifyAll(notification);
                pendingList.remove(notification);
            } 
        }
    }

    public ReceiptStatus receive(INotification message) throws Exception {
        return ReceiptStatus.NotProcessed;
    }

    // stub - maybe this could act as a bridge by some method - probably providing a transmitter to pass on to - such as the global transmitter
    // during construction.
    Date nextExec =new Date(0L);
    /**
    * Frequency at which to sleep when waiting for message (WaitForMessage) and queue has pending items.
    * NOTE: The individual items should also use the IsReady to prevent too frequent notification of recipients
    */
    private int __PendingFrequencyMs;
    public int getPendingFrequencyMs() {
        return __PendingFrequencyMs;
    }

    public void setPendingFrequencyMs(int value) {
        __PendingFrequencyMs = value;
    }

    /**
    * Amount of time to sleep when waiting for messages when queue has pending items.
    * This
    */
    private int __PendingSleepMs;
    public int getPendingSleepMs() {
        return __PendingSleepMs;
    }

    public void setPendingSleepMs(int value) {
        __PendingSleepMs = value;
    }

    public void waitForMessage() throws Exception {
        if (pendingList.getitems().size() > 0)
        {
            if (nextExec.before(Calendar.getInstance().getTime()))
                Thread.sleep(getPendingSleepMs());

            Calendar d2 = Calendar.getInstance();
            d2.add(Calendar.MILLISECOND, getPendingFrequencyMs());
            nextExec = d2.getTime();
            return ;
        }
         
        // no need to wait when already a message pending.
        Calendar d2 = Calendar.getInstance();
        d2.add(Calendar.MILLISECOND, 33);
        nextExec = d2.getTime();
        messageWaitEvent.waitOne();
    }

}



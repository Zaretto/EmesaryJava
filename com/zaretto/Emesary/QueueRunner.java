package com.zaretto.Emesary;

/**
* background queue processing.
*/
public class QueueRunner   
{
    QueuedTransmitter qt;
    boolean sessionOpened = false;
    public QueueRunner(QueuedTransmitter t) throws Exception {
        qt = t;
    }

    public void queueRun() throws Exception {
        if (!sessionOpened)
        {
            sessionOpened = true;
        }
         
        while (!getStopQueueRequest())
        {
            qt.processPending();
            qt.waitForMessage();
        }
    }

    /**
    * true to request the queue processing to stop at the next convenient moment
    */
    private boolean __StopQueueRequest;
    public boolean getStopQueueRequest() {
        return __StopQueueRequest;
    }

    public void setStopQueueRequest(boolean value) {
        __StopQueueRequest = value;
    }

}



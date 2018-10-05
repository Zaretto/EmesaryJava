package com.zaretto.Emesary;

public class RouteToQueueBridge   implements IReceiver
{
    /**
    * Create queue forwarder
    * 
    *  @param linkedQueue Queue to forward to
    *  @param FinishAfterForwarded Whether or not to finish as a pending operation
    */
    public RouteToQueueBridge(QueuedTransmitter linkedQueue, boolean FinishAfterForwarded) throws Exception {
        this.setLinkedQueue(linkedQueue);
        if (FinishAfterForwarded)
            this.setReturnReceipt(ReceiptStatus.PendingFinished);
        else
            this.setReturnReceipt(ReceiptStatus.Pending);
    }

    public RouteToQueueBridge(QueuedTransmitter linkedQueue, ReceiptStatus returnReceipt) throws Exception {
        this.setLinkedQueue(linkedQueue);
        this.setReturnReceipt(returnReceipt);
    }

    /**
    * Receive and forward to queue.
    * 
    *  @param message 
    *  @return
    */
    public ReceiptStatus receive(INotification message) throws Exception {
        if (message instanceof QueueNotification && getLinkedQueue() != null)
        {
            getLinkedQueue().NotifyAll(message);
            return getReturnReceipt();
        }
         
        return ReceiptStatus.NotProcessed;
    }

    private QueuedTransmitter __LinkedQueue;
    public QueuedTransmitter getLinkedQueue() {
        return __LinkedQueue;
    }

    public void setLinkedQueue(QueuedTransmitter value) {
        __LinkedQueue = value;
    }

    private ReceiptStatus __ReturnReceipt = ReceiptStatus.OK;
    public ReceiptStatus getReturnReceipt() {
        return __ReturnReceipt;
    }

    public void setReturnReceipt(ReceiptStatus value) {
        __ReturnReceipt = value;
    }

}



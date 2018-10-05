package com.zaretto.Emesary;

/**
 * Created by Richard on 05/10/2018.
 */

public interface IAsyncOperationCompleted {
    ReceiptStatus OperationCompleted(IQueueNotification notification, ReceiptStatus inStatus);
}

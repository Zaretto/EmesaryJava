package com.zaretto.Emesary;

/*---------------------------------------------------------------------------
 *
 *	Title                : EMESARY Global Queue
 *
 *	File Type            : Implementation File
 *
 *	Description          : A Global queue is a convenience for systems that only require
 *	                     : a single queue.
 * 
 *  References           : http://www.chateau-logic.com/content/class-based-inter-object-communication
 *
 *	Author               : Richard Harrison (richard@zaretto.com)
 *
 *	Creation Date        : 24 October 2011
 *
 *	Version              : $Header: $
 *
 *  Copyright © 2011 Richard Harrison           All Rights Reserved.
 *
 *---------------------------------------------------------------------------*/
public class GlobalQueue   
{
    private static QueuedTransmitter globalQueue = new QueuedTransmitter("global-queue");
    public static QueuedTransmitter getqueue() throws Exception {
        return globalQueue;
    }

    public static void register(IReceiver R) throws Exception {
        globalQueue.register(R);
    }

    public static void deRegister(IReceiver R) throws Exception {
        globalQueue.deRegister(R);
    }

    public static ReceiptStatus NotifyAll(INotification M) throws Exception {
        return globalQueue.NotifyAll(M);
    }

}



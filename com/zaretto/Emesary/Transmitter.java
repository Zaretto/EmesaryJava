package com.zaretto.Emesary;

/*---------------------------------------------------------------------------
 *
 *	Title                : EMESARY Class based inter-object communication
 *
 *	File Type            : Implementation File
 *
 *	Description          : Provides generic inter-object communication. For an object to receive a message it
 *	                     : must first register with a Transmitter, such as GlobalTransmitter, and implement the 
 *	                     : IReceiver interface. That's it.
 *	                     : To send a message use a Transmitter with an object. That's all there is to it.
 *	                     : 
 * 
 *  References           : http://www.chateau-logic.com/content/class-based-inter-object-communication
 *
 *	Author               : Richard Harrison (richard@zaretto.com)
 *
 *	Creation Date        : 24 September 2009
 *
 *	Version              : $Header: $
 *
 *  Copyright Â© 2009 Richard Harrison           All Rights Reserved.
 *
 *---------------------------------------------------------------------------*/

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
     * Description: Transmits Message derived objects. Each instance of this class provides a
     * databus to which any number of receivers can attach to. 
     *
     * Messages may be inherited and customised between individual systems.
     */
public class Transmitter   implements ITransmitter
{
    private ConcurrentHashMap<IReceiver, Integer> V = new ConcurrentHashMap<IReceiver, Integer>();
    private AtomicInteger CurrentRecipientIndex = new AtomicInteger(0);
    /**
                * Registers an object to receive messsages from this transmitter. 
                * This object is added to the top of the list of objects to be notified. This is deliberate as 
                * the sequence of registration and message receipt can influence the way messages are processing
                * when ReceiptStatus of Abort or Finished are encountered. So it was a deliberate decision that the
                * most recently registered recipients should process the messages/events first.
                */
    public void register(IReceiver R) throws Exception {
        if (!V.contains(R))
        {
            V.put(R, CurrentRecipientIndex.getAndAdd(1));
        }
    }

    /*
                *  Removes an object from receving message from this transmitter
                */
    public void deRegister(IReceiver R) throws Exception {
        V.remove(R);
    }

    /*
                * Notify all registered recipients. Stop when receipt status of abort or finished are received.
                * The receipt status from this method will be 
                *  - OK > message handled
                *  - Fail > message not handled. A status of Abort from a recipient will result in our status
                *           being fail as Abort means that the message was not and cannot be handled, and
                *           allows for usages such as access controls.
                * NOTE: When I first designed Emesary I always intended to have message routing and the ability
                *       for each recipient to specify an area of interest to allow performance improvements
                *       however this has not yet been implemented - but the concept is still there and
                *       could be implemented by extending the IReceiver interface to allow for this.
                */
    public ReceiptStatus NotifyAll(INotification M) throws Exception {
        ReceiptStatus return_status = ReceiptStatus.NotProcessed;
        try
        {
            for (Object __dummyForeachVar0 : V.keySet())
            {
                IReceiver R = (IReceiver)__dummyForeachVar0;
                if (R != null)
                {
                    ReceiptStatus rstat = R.receive(M);
                    switch(rstat)
                    {
                        case Fail: 
                            return_status = ReceiptStatus.Fail;
                            break;
                        case Pending: 
                            return_status = ReceiptStatus.Pending;
                            break;
                        case PendingFinished: 
                            return rstat;
                        case NotProcessed: 
                            break;
                        case OK: 
                            if (return_status == ReceiptStatus.NotProcessed)
                                return_status = rstat;
                             
                            break;
                        case Abort: 
                            return ReceiptStatus.Abort;
                        case Finished: 
                            return ReceiptStatus.OK;
                    
                    }
                }
                 
            }
        }
        catch (Exception __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }

        return return_status;
    }

    // return_status = ReceiptStatus.Abort;
    public static boolean failed(ReceiptStatus receiptStatus) throws Exception {
        return receiptStatus == ReceiptStatus.Fail || receiptStatus == ReceiptStatus.Abort;
    }
}


//
// failed is either Fail or Abort.
// NotProcessed isn't a failure because it hasn't been processed.
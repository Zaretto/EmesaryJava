package com.zaretto.Emesary;


public enum ReceiptStatus
{
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
     *  Copyright © 2009 Richard Harrison           All Rights Reserved.
     *
     *---------------------------------------------------------------------------*/
    /**
    * Processing completed successfully
    */
    OK,
    /**
    * Individual item failure
    */
    Fail,
    /**
    * Fatal error; stop processing any further recipieints of this message. Implicitly fail
    */
    Abort,
    // stop processing this event and fail
    /**
    * Definitive completion - do not send message to any further recipieints
    */
    Finished,
    // stop processing this event and return success
    /**
    * Return value when method doesn't process a message.
    */
    NotProcessed,
    // recipient didn't recognise this event
    /**
    * Message has been sent but the return status cannot be determined as it has not been processed by the recipient.
    * 
    * For example a queue or outgoing bridge
    */
    Pending,
    // Not yet processed
    /**
    * Message has been definitively handled but the return value cannot be determined. The message will not be sent any further
    * 
    * For example a point to point forwardeing bridge
    */
    PendingFinished
}

// definitively not yet processed  (e.g. forwarded to queue)
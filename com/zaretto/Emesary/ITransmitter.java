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
 *  Copyright © 2009 Richard Harrison           All Rights Reserved.
 *
 *---------------------------------------------------------------------------*/
/**
        * Description: Transmits Message derived objects. Each instance of this class provides a
        * databus to which any number of receivers can attach to. 
        *
        * Messages may be inherited and customised between individual systems.
        */
public interface ITransmitter   
{
    void register(IReceiver R) throws Exception ;

    /*
                *  Removes an object from receving message from this transmitter
                */
    void deRegister(IReceiver R) throws Exception ;

    /*
                * Notify all registered recipients. Stop when receipt status of abort or finished are received.
                * The receipt status from this method will be 
                *  - OK > message handled
                *  - Fail > message not handled. A status of Abort from a recipient will result in our status
                *           being fail as Abort means that the message was not and cannot be handled, and
                *           allows for usages such as access controls.
                */
    ReceiptStatus NotifyAll(INotification M) throws Exception ;

}



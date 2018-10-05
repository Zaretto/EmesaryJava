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
/*
     * Mainly a convenience - the global transmitter is exactly what it sounds like - an instance
     * of a transmitter that can be used globally.
     */
public class GlobalTransmitter   
{
    private static Transmitter _globalNotifier = new Transmitter();
    public static Transmitter getTransmitter() throws Exception {
        return _globalNotifier;
    }

    public static void register(IReceiver R) throws Exception {
        _globalNotifier.register(R);
    }

    public static void deRegister(IReceiver R) throws Exception {
        _globalNotifier.deRegister(R);
    }

    public static ReceiptStatus NotifyAll(INotification M) throws Exception {
        return _globalNotifier.NotifyAll(M);
    }

}



/* License
 * 
 * Copyright 1994-2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *  
 *  * Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *  * Redistribution in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility. 
 */

//
// $Workfile: Wallet.java $
// $Revision: 5 $
// $Date: 5/02/00 9:05p $
// $Author: Akuzmin $
// $Archive: /Products/Europa/samples/com/sun/javacard/samples/wallet/Wallet.java $
// $Modtime: 5/02/00 8:48p $
// Original author: Zhiqun Chen
//

package com.sun.javacard.samples.wallet;

import javacard.framework.ISOException;
import javacard.framework.ISO7816;
import javacard.framework.APDU;
import javacard.framework.OwnerPIN;
import javacard.framework.Applet;

public class Wallet extends Applet {

  /* constants declaration */

  // code of CLA byte in the command APDU header
  final static byte Wallet_CLA =(byte)0xB0;

  // codes of INS byte in the command APDU header
  final static byte VERIFY = (byte) 0x20;
  final static byte CREDIT = (byte) 0x30;
  final static byte DEBIT = (byte) 0x40;
  final static byte GET_BALANCE = (byte) 0x50;

  // maximum balance
  final static short MAX_BALANCE = 0x7FFF;
  // maximum transaction amount
  final static byte MAX_TRANSACTION_AMOUNT = 127;

  // maximum number of incorrect tries before the
  // PIN is blocked
  final static byte PIN_TRY_LIMIT =(byte)0x03;
  // maximum size PIN
  final static byte MAX_PIN_SIZE =(byte)0x08;

  // signal that the PIN verification failed
  final static short SW_VERIFICATION_FAILED =
0x6300;
  // signal the the PIN validation is required
  // for a credit or a debit transaction
  final static short SW_PIN_VERIFICATION_REQUIRED =
                                            0x6301;
  // signal invalid transaction amount
  // amount > MAX_TRANSACTION_AMOUNT or amount < 0
  final static short SW_INVALID_TRANSACTION_AMOUNT = 0x6A83;

  // signal that the balance exceed the maximum
  final static short SW_EXCEED_MAXIMUM_BALANCE =
0x6A84;
  // signal the the balance becomes negative
  final static short SW_NEGATIVE_BALANCE = 0x6A85;

  /* instance variables declaration */
    OwnerPIN pin;
  short balance;

  private Wallet (byte[] bArray,short bOffset,byte bLength){

    // It is good programming practice to allocate
    // all the memory that an applet needs during
    // its lifetime inside the constructor
    pin = new OwnerPIN(PIN_TRY_LIMIT,   MAX_PIN_SIZE);

    // The installation parameters contain the PIN
    // initialization value
    pin.update(bArray, bOffset, bLength);
    register();

  } // end of the constructor

  public static void install(byte[] bArray, short bOffset, byte bLength){
    // create a Wallet applet instance
    new Wallet(bArray, bOffset, bLength);
  } // end of install method
  public boolean select() {

    // The applet declines to be selected
    // if the pin is blocked.
    if ( pin.getTriesRemaining() == 0 )
       return false;

    return true;

  }// end of select method
  public void deselect() {

    // reset the pin value
    pin.reset();

  }
    public void process(APDU apdu) {

    // APDU object carries a byte array (buffer) to
    // transfer incoming and outgoing APDU header
    // and data bytes between card and CAD

    // At this point, only the first header bytes
    // [CLA, INS, P1, P2, P3] are available in
    // the APDU buffer.
    // The interface javacard.framework.ISO7816
    // declares constants to denote the offset of
    // these bytes in the APDU buffer

    byte[] buffer = apdu.getBuffer();
    // check SELECT APDU command
    if ((buffer[ISO7816.OFFSET_CLA] == 0) &&
       (buffer[ISO7816.OFFSET_INS] == (byte)(0xA4)) )
      return;
    // verify the reset of commands have the
    // correct CLA byte, which specifies the
    // command structure
    if (buffer[ISO7816.OFFSET_CLA] != Wallet_CLA)
       ISOException.throwIt
(ISO7816.SW_CLA_NOT_SUPPORTED);
    switch (buffer[ISO7816.OFFSET_INS]) {
      case GET_BALANCE:   getBalance(apdu);
                          return;
      case DEBIT:         debit(apdu);
                          return;
      case CREDIT:        credit(apdu);
                          return;
      case VERIFY:        verify(apdu);
                          return;
      default:       ISOException.throwIt
                    (ISO7816.SW_INS_NOT_SUPPORTED);
    }

 }   // end of process method

 private void credit(APDU apdu) {

   // access authentication
     if ( ! pin.isValidated() )
 ISOException.throwIt(
        SW_PIN_VERIFICATION_REQUIRED);

    byte[] buffer = apdu.getBuffer();

    // Lc byte denotes the number of bytes in the
    // data field of the command APDU
    byte numBytes = buffer[ISO7816.OFFSET_LC];


    // indicate that this APDU has incoming data
    // and receive data starting from the offset
    // ISO7816.OFFSET_CDATA following the 5 header
    // bytes.
    byte byteRead =
              (byte)(apdu.setIncomingAndReceive());

    // it is an error if the number of data bytes
    // read does not match the number in Lc byte
    if ( ( numBytes != 1 ) || (byteRead != 1) )
     ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);

    // get the credit amount
    byte creditAmount =
                  buffer[ISO7816.OFFSET_CDATA];

    // check the credit amount
    if ( ( creditAmount > MAX_TRANSACTION_AMOUNT)
         || ( creditAmount < 0 ) )
        ISOException.throwIt
                   (SW_INVALID_TRANSACTION_AMOUNT);

    // check the new balance
    if ( (short)( balance + creditAmount)  > MAX_BALANCE )
       ISOException.throwIt
                     (SW_EXCEED_MAXIMUM_BALANCE);


    // credit the amount
    balance = (short)(balance + creditAmount);

  } // end of deposit method

    private void debit(APDU apdu) {

    // access authentication
      if ( ! pin.isValidated() )
       ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);

    byte[] buffer = apdu.getBuffer();

    byte numBytes =
            (byte)(buffer[ISO7816.OFFSET_LC]);

    byte byteRead =
            (byte)(apdu.setIncomingAndReceive());

    if ( ( numBytes != 1 ) || (byteRead != 1) ) {
	ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    }

    // get debit amount
    byte debitAmount =
                     buffer[ISO7816.OFFSET_CDATA];

    // check debit amount
    if ( ( debitAmount > MAX_TRANSACTION_AMOUNT)
         ||  ( debitAmount < 0 ) ) {
	ISOException.throwIt(SW_INVALID_TRANSACTION_AMOUNT);
    }

    // check the new balance
    if ( (short)( balance - debitAmount ) < (short)0 ) {
	ISOException.throwIt(SW_NEGATIVE_BALANCE);
    }

    balance = (short) (balance - debitAmount);

  } // end of debit method

  private void getBalance(APDU apdu) {

    byte[] buffer = apdu.getBuffer();

    // inform system that the applet has finished
    // processing the command and the system should
    // now prepare to construct a response APDU
    // which contains data field
    short le = apdu.setOutgoing();

    if ( le < 2 )
       ISOException.throwIt
                       (ISO7816.SW_WRONG_LENGTH);

    //informs the CAD the actual number of bytes
    //returned
    apdu.setOutgoingLength((byte)2);

    // move the balance data into the APDU buffer
    // starting at the offset 0
    buffer[0] = (byte)(balance >> 8);
    buffer[1] = (byte)(balance & 0xFF);

    // send the 2-byte balance at the offset
    // 0 in the apdu buffer
    apdu.sendBytes((short)0, (short)2);

  } // end of getBalance method

    private void verify(APDU apdu) {

    byte[] buffer = apdu.getBuffer();
    // retrieve the PIN data for validation.
  byte byteRead = 
            (byte)(apdu.setIncomingAndReceive());

  // check pin
    // the PIN data is read into the APDU buffer
  // at the offset ISO7816.OFFSET_CDATA
  // the PIN data length = byteRead
  if ( pin.check(buffer, ISO7816.OFFSET_CDATA,
			    byteRead) == false )
     ISOException.throwIt
                      (SW_VERIFICATION_FAILED);

  } // end of validate method
} // end of class Wallet


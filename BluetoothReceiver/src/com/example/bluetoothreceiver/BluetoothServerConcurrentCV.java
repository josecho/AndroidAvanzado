package com.example.bluetoothreceiver;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BluetoothServerConcurrentCV {
	private BluetoothManagerThreadCV btMgr;
	// The thread managing all calls (class definition see below)
	private BluetoothServerCV btServ;

	// The BluetoothServerCV object managed by the thread
	/*
	 * Constructor. A call will create and start a thread. This thread will
	 * create a BluetoothServerCV object and manage read/write requests for it.
	 * Parameters: serviceName - Name of the offered service (if null, some
	 * default value will be used). uuidStringParam - UUID identifying the
	 * offered service (if null, some default value will be used).
	 */
	public BluetoothServerConcurrentCV(String serviceName, String uuidString) {
		btMgr = new BluetoothManagerThreadCV(serviceName, uuidString);
		btMgr.start();
	}

	// Two public methods to send data (return value = success)
	public boolean send(String data) {
		return send(data.getBytes());
	}

	public boolean send(byte[] data) {
		// Prepare the send order (as a Message object containing the data)
		Bundle bdl = new Bundle();
		bdl.putString("Operation", "Send");
		bdl.putByteArray("Data", data);
		Message msg = new Message();
		msg.setData(bdl);
		// Submit the order to the managing thread
		boolean success = btMgr.execute(msg);
		return success;
	}

	/*
	 * A public method to receive data. Parameters: handler – A handler of the
	 * thread that calls this method. The handler will handle the received data
	 * for this thread: After a successful read() from the Bluetooth socket, the
	 * btMgr thread will call handleMessage() of this handler with the received
	 * data. The parameter of this call will be a Message object containing a
	 * bundle with two entries: ("NumberOfBytes",int): The number of valid bytes
	 * trans- ferred in the byte array below. ("Data",byte[]): A byte array with
	 * the received data. maxNo - The maximum number of bytes that can be
	 * received. Return value: Success of the operation.
	 */
	public boolean receive(BluetoothCallbackHandlerCV handler, int maxNo) {
		// Prepare the receive order (as a Message object containing
		// a handler to be executed on the received data)
		Bundle bdl = new Bundle();
		bdl.putString("Operation", "Receive");
		bdl.putSerializable("Handler", handler);
		bdl.putInt("MaxNo", maxNo);
		Message msg = new Message();
		msg.setData(bdl);
		// Submit the order to the managing thread
		boolean success = btMgr.execute(msg);
		return success;
	}

	/*
	 * A second public method to receive data. It differs from the first method
	 * in the parameter ‘requestedNoOfBytes’: Only after receiving this number
	 * of bytes, the bytes received will be returned to the caller. This might
	 * require a whole sequence of read operations on the socket.
	 */
	public boolean receiveFully(BluetoothCallbackHandlerCV handler,
			int requestedNoOfBytes) {
		Bundle bdl = new Bundle();
		bdl.putString("Operation", "ReceiveFully");
		bdl.putSerializable("Handler", handler);
		bdl.putInt("RequestedNumberOfBytes", requestedNoOfBytes);
		Message msg = new Message();
		msg.setData(bdl);
		boolean success = btMgr.execute(msg);
		return success;
	}

	// A method to stop the btMgr thread. btMgr will close the socket.
	public void close() {
		btMgr.cancel();
	}

	// Class definition for the managing thread
	private class BluetoothManagerThreadCV extends Thread {
		private BluetoothOrderHandlerCV orderHandler;
		// Handler handling orders to this thread
		private String serviceName;
		// Name of the offered service
		private String uuidString;

		// UUID identifying the offered service
		BluetoothManagerThreadCV(String serviceName, String uuidString) {
			this.serviceName = serviceName;
			this.uuidString = uuidString;
		}

		// Run loop of the thread, realized by a Looper
		public void run() {
			// Create a message queue for orders to this thread
			Looper.prepare();
			// Create a handler handling orders to this thread
			orderHandler = new BluetoothOrderHandlerCV();
			// Create the communication endpoint
			btServ = new BluetoothServerCV(serviceName, uuidString);
			// Start the loop to take and handle orders
			Looper.loop();
		}

		/*
		 * A method used to submit execution orders to this thread. An order is
		 * defined by a Message object containing a bundle. The bundle contains
		 * a String component "Operation" defining the operation to be executed
		 * and a second component for the data. Name and type of these data
		 * depend on the operation.
		 */
		public boolean execute(Message msg) {
			// If the orderHandler does not exist yet, wait at most
			// five seconds to give it a chance (a somewhat dirty hack).
			for (int i = 0; i < 10; i++)
				if (orderHandler == null)
					try {
						sleep(500);
					} catch (Exception e) {
					}
			;
			// If the connection is still not open, give up.
			if (orderHandler == null)
				return false;
			orderHandler.sendMessage(msg);
			return true;
		}

		// A method to close the connection and stop the looping thread
		public void cancel() {
			btServ.close();
			orderHandler.getLooper().quit();
		}

		// Class definition for the handler
		// that shall execute the orders submitted to this thread
		private class BluetoothOrderHandlerCV extends Handler {
			public void handleMessage(Message msg) {
				Bundle msgDataBundle = msg.getData();
				String operation = msgDataBundle.getString("Operation");
				// Execute a “Send” order
				if (operation.equals("Send"))
					btServ.send(msgDataBundle.getByteArray("Data"));
				// Execute a “Receive” order (see above comments on
				// receive() in BluetoothServerConcurrentCV)
				if (operation.equals("Receive")) {
					BluetoothCallbackHandlerCV callbackHandler = (BluetoothCallbackHandlerCV) msgDataBundle
							.getSerializable("Handler");
					int maxNo = msgDataBundle.getInt("MaxNo");
					byte receivedData[] = new byte[maxNo];
					int noReceived = btServ.receive(receivedData);
					Bundle replyBundle = new Bundle();
					replyBundle.putInt("NumberOfBytes", noReceived);
					replyBundle.putByteArray("Data", receivedData);
					Message replyMessage = new Message();
					replyMessage.setData(replyBundle);
					callbackHandler.sendMessage(replyMessage);
				}
				// Execute a “ReceiveFully” order
				if (operation.equals("ReceiveFully")) {
					BluetoothCallbackHandlerCV callbackHandler = (BluetoothCallbackHandlerCV) msgDataBundle
							.getSerializable("Handler");
					int reqNumberOfBytes = msgDataBundle
							.getInt("RequestedNumberOfBytes");
					byte receivedData[] = new byte[reqNumberOfBytes];
					int noReceived = 0;
					do {
						byte buffer[] = new byte[10000];
						int rec = btServ.receive(buffer);
						for (int i = 0; i < rec; i++)
							receivedData[noReceived + i] = buffer[i];
						noReceived += rec;
					} while (noReceived < reqNumberOfBytes);
					Bundle replyBundle = new Bundle();
					replyBundle.putInt("NumberOfBytes", noReceived);
					replyBundle.putByteArray("Data", receivedData);
					Message replyMessage = new Message();
					replyMessage.setData(replyBundle);
					callbackHandler.sendMessage(replyMessage);
				}
			}
		}
	}

	/*
	 * Class definition for the callback handler of the calling thread. The
	 * handler is used to notify this thread about the result of a Bluetooth
	 * operation. It is especially called after completion of a concurrently
	 * executed Bluetooth read() operation to transfer the received data to the
	 * receiver thread - the read() operation will activate the handleMessage()
	 * method of the handler. The parameter of this call will be a Message
	 * object containing a bundle with two entries: ("NumberOfBytes",int): The
	 * number of valid bytes transferred in the byte array below.
	 * ("Data",byte[]): A byte array with the received data. Therefore, the
	 * programmer of the receiving thread must define a subclass of
	 * BluetoothCallbackHandlerCV overwriting the void handleMessage(Message
	 * msg) method. This method defines the operations the receiving thread
	 * wants to execute on the received data.
	 */
	public static abstract class BluetoothCallbackHandlerCV extends Handler
			implements Serializable {
	}
}

package com.example.bluetoothsender;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BluetoothConnectionToServerConcurrentCV {
	private BluetoothManagerThreadCV btMgr;
	// The thread managing this Bluetooth connection
	private BluetoothConnectionToServerCV btConn;

	// The Bluetooth connection that is managed by the thread
	/*
	 * Constructor. A call will create and start a thread. This thread will
	 * create a BluetoothConnectionToServerCV object and manage read/write
	 * requests for it. Parameters: partnerDeviceName - Name of the device to
	 * connect to (if null or no device of this name is available, the first
	 * device in the list of paired devices will be used). uuidStringParam -
	 * UUID identifying the service of the partner (if null, some default value
	 * will be used).
	 */
	public BluetoothConnectionToServerConcurrentCV(String partnerDeviceName,
			String uuidString) {
		btMgr = new BluetoothManagerThreadCV(partnerDeviceName, uuidString);
		btMgr.start();
	}

	// Two methods to send data (return value = success)
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
	 * A method to receive data. The parameter is a handler defined by the
	 * calling thread to handle the received data. After a successful read()
	 * from the Bluetooth socket, the management thread will call
	 * handleMessage() of this handler with the received data. The parameter of
	 * this call will be a Message object containing a bundle with two entries:
	 * ("NumberOfBytes",int): The number of valid bytes transferred in the byte
	 * array below. ("Data",byte[]): A byte array with the received data. The
	 * return value indicates whether the operation has been successful.
	 */
	public boolean receive(BluetoothCallbackHandlerCV handler) {
		// Prepare the receive order (as a Message object containing
		// a handler to be executed on the received data)
		Bundle bdl = new Bundle();
		bdl.putString("Operation", "Receive");
		bdl.putSerializable("Handler", handler);
		Message msg = new Message();
		msg.setData(bdl);
		// Submit the order to the managing thread
		boolean success = btMgr.execute(msg);
		return success;
	}

	// A method to stop the thread and close the communication socket
	public void close() {
		btMgr.cancel();
	}

	// Class definition for the managing thread
	private class BluetoothManagerThreadCV extends Thread {
		private BluetoothOrderHandlerCV orderHandler;
		// Handler with the handleMessage() method
		// handling orders to this thread
		private String partnerDeviceName;
		// Name of the device to connect to
		private String uuidString;

		// UUID identifying the service to connect to
		BluetoothManagerThreadCV(String partnerDeviceName, String uuidString) {
			this.partnerDeviceName = partnerDeviceName;
			this.uuidString = uuidString;
		}

		// Run loop of the thread, realized by a Looper
		public void run() {
			// Create a message queue for orders to this thread
			Looper.prepare();
			// Create a handler handling orders to this thread
			orderHandler = new BluetoothOrderHandlerCV();
			// Open a connection to the server
			btConn = new BluetoothConnectionToServerCV(partnerDeviceName,
					uuidString);
			// Start the loop to take and handle orders
			Looper.loop();
		}

		/*
		 * A method used to submit execution orders to this thread. An order is
		 * defined by a Message object containing a bundle. The bundle contains
		 * a String component "Operation" defining the operation to be executed
		 * and a second component for the data. The name and the type of this
		 * component depend on the operation.
		 */
		public boolean execute(Message msg) {
			// If the orderHandler does not exist yet,
			// wait at most five seconds to give it a chance
			for (int i = 0; i < 10; i++)
				if (orderHandler == null)
					try {
						sleep(500);
					} catch (Exception e) {
					}
			;
			// If the connection is still not open, give up
			if (btConn == null)
				return false;
			orderHandler.sendMessage(msg);
			return true;
		}

		// A method to close the connection and stop the looping thread
		public void cancel() {
			btConn.close();
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
					btConn.send(msgDataBundle.getByteArray("Data"));
				// Execute a “Receive” order (see above comments on receive()
				// in BluetoothConnectionToServerConcurrentCV)
				if (operation.equals("Receive")) {
					BluetoothCallbackHandlerCV callbackHandler = (BluetoothCallbackHandlerCV) msgDataBundle
							.getSerializable("Handler");
					byte receivedData[] = new byte[1024];
					int noReceived = btConn.receive(receivedData);
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
	 * Class definition for the callback handler of the calling thread (for
	 * detailed comments see class BluetoothServerConcurrentCV)
	 */
	public static abstract class BluetoothCallbackHandlerCV extends Handler
			implements Serializable {
	}
}

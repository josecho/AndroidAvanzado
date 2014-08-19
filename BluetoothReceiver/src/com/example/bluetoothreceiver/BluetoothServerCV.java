package com.example.bluetoothreceiver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BluetoothServerCV {
	private BluetoothAdapter btAdapter;
	// Bluetooth adapter for the device this app is running on
	private String serviceName;
	// Name of the service offered (for SDP entry)
	private UUID uuid;
	// UUID of the service offered (for SDP entry)
	private BluetoothServerSocket servSocket;
	// Server socket offered; an external client can connect to it
	private BluetoothSocket commSocket;
	// Socket for the communication
	// after the connection has been established
	private InputStream inStream;
	// InputStream of commSocket
	private OutputStream outStream;

	// OutputStream of commSocket
	/*
	 * Constructor. A call will wait for an incoming connection request of a
	 * client and then open a Bluetooth connection to this client, creating a
	 * new communication socket commSocket for this connection. Parameters:
	 * serviceName - Name of the service offered (if null, some default value
	 * will be used). uuidStringParam - UUID identifying the service offered (if
	 * null, some default value will be used).
	 */
	public BluetoothServerCV(String serviceName, String uuidString) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			System.out.println("Error: Bluetooth is not supported");
			return;
		}
		if (!btAdapter.isEnabled()) {
			System.out.println("Error: Bluetooth is not switched on");
			return;
		}
		if (serviceName == null || serviceName.equals(""))
			this.serviceName = "Receive Service";
		else
			this.serviceName = serviceName;
		if (uuidString == null || uuidString.equals(""))
			this.uuid = UUID.fromString("36AE13EE-7CC3-4ABC-A060-B5E4D4317904");
		// UUID Strings have the format xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
		// where the x’s are hexadecimal digits
		// and y is a hexadecimal digit between 8 and F.
		else
			this.uuid = UUID.fromString(uuidString);
		try {
			// Create a server socket
			servSocket = btAdapter.listenUsingRfcommWithServiceRecord(this.serviceName, this.uuid);
			// Wait for a client connect() and accept it then
			commSocket = servSocket.accept();
			// Close the server socket
			// as no more connection requests shall be accepted
			servSocket.close();
			// Get input and output streams of the new communication socket
			inStream = new DataInputStream(commSocket.getInputStream());
			outStream = new DataOutputStream(commSocket.getOutputStream());
		} catch (Exception e) {
			Error message;
			return;
		}
	}

	// Two methods to send data through commSocket
	public void send(String data) {
		send(data.getBytes());
	}

	public void send(byte[] data) {
		try {
			outStream.write(data);
		} catch (IOException e) {
			Error message;
			return;
		}
	}

	/*
	 * A method to receive data through commSocket. The received data will be
	 * returned in the buffer parameter. The return value of the method will be
	 * the number of received bytes or -1 in case of an error.
	 */
	public int receive(byte[] buffer) {
		try {
			int numberOfBytesReceived = inStream.read(buffer);
			return numberOfBytesReceived;
		} catch (IOException e) {
			Error message;
			return -1;
		}
	}

	// A method to close commSocket
	public void close() {
		try {
			commSocket.close();
		} catch (IOException e) {
			Error message;
			return;
		}
	}
}

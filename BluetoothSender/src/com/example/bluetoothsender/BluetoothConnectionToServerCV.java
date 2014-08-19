package com.example.bluetoothsender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothConnectionToServerCV {
	private BluetoothAdapter btAdapter;
	// Bluetooth adapter for the device this app is running on
	private BluetoothDevice partnerDevice;
	// The partner (server) device to communicate with
	private UUID uuid;
	// UUID (Universally Unique Identifier) identifying the server
	private BluetoothSocket commSocket;
	// Socket connected to the server
	// and to be used for the communication
	private InputStream inStream;
	// InputStream of commSocket
	private OutputStream outStream;
	// OutputStream of commSocket

	/*
	 * Constructor. A call will open a Bluetooth connection to a server.
	 * Afterwards, the attribute commSocket references the client socket.
	 * Parameters: partnerDeviceName - Name of the device to connect to (if null
	 * or no device of this name is available, the first device in the list of
	 * paired devices will be used). uuidStringParam - UUID identifying the
	 * service of the partner (if null, some default value is used).
	 */
	public BluetoothConnectionToServerCV(String partnerDeviceName,
			String uuidString) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			System.out.println("Error: Bluetooth is not supported");
			return;
		}
		if (!btAdapter.isEnabled()) {
			System.out.println("Error: Bluetooth is not switched on");
			return;
		}
		partnerDevice = determinePartnerDevice(partnerDeviceName);
		if (partnerDevice == null) {
			System.out.println("Error: No partner device available");
			return;
		}
		if (uuidString == null || uuidString.equals(""))
			this.uuid = UUID.fromString("12345678-4321-4111-ADDA-345127542950");
		// UUID Strings have the format xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
		// where the x’s are hexadecimal digits
		// and y is a hexadecimal digit between 8 and F.
		else
			this.uuid = UUID.fromString(uuidString);
		try {
			// Create a socket
			commSocket = partnerDevice.createRfcommSocketToServiceRecord(uuid);
			// Connect it to the server
			commSocket.connect();
			// Get the input and output streams of commSocket
			inStream = new DataInputStream(commSocket.getInputStream());
			outStream = new DataOutputStream(commSocket.getOutputStream());
		} catch (Exception e) {
			Error message;
			//System.out.println(e);
			return;
		}
	}

	/*
	 * A helper method to determine the partner device to communicate with: - If
	 * there are no paired devices, null is returned. - If there is a paired
	 * device with the name 'devname', this device is returned. - If there are
	 * paired devices but none with the name 'devname', the first entry in the
	 * list of paired devices is returned.
	 */
	private BluetoothDevice determinePartnerDevice(String devname) {
		Set<BluetoothDevice> bondedDevs = btAdapter.getBondedDevices();
		if (bondedDevs.size() == 0)
			return null;
		for (Iterator<BluetoothDevice> it = bondedDevs.iterator(); it.hasNext();) {
			BluetoothDevice btd = it.next();
			if (btd.getName().equals(devname))
				return btd;
		}
		return bondedDevs.iterator().next();
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
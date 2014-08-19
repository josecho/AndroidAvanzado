package com.example.bluetoothreceiver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

public class BluetoothReceiver extends Activity {

	static EditText outputView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		outputView = (EditText) findViewById(R.id.receivedText);
		String servname = "Receive Service";
		String uuid = "36AE13EE-7CC3-4ABC-A060-B5E4D4317904";
		BluetoothServerConcurrentCV btServ = new BluetoothServerConcurrentCV(
				servname, uuid);
		btServ.receive(new CallbackHandlerTextOutput(), 4096);
	}

	public static class CallbackHandlerTextOutput extends
			BluetoothServerConcurrentCV.BluetoothCallbackHandlerCV {
		public void handleMessage(Message msg) {
			byte[] receivedData = msg.getData().getByteArray("Data");
			int noBytes = msg.getData().getInt("NumberOfBytes");
			String recString = (new String(receivedData)).substring(0, noBytes);
			outputView.setText(recString);
		}
	}

}

package com.example.bluetoothsender;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BluetoothSender extends Activity {
	
	private BluetoothConnectionToServerConcurrentCV btConn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//String devname = "GT-S557OI";
		String devname = "GalaxyS";
		String uuid = "36AE13EE-7CC3-4ABC-A060-B5E4D4317904";
		btConn =new BluetoothConnectionToServerConcurrentCV(devname,uuid);
	}
	
	public void send(View v) {
		EditText et = (EditText) findViewById(R.id.textToBeSent);
		String text = et.getText().toString();
		btConn.send(text);
		}

}

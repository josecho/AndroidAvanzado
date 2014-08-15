package com.example.gui_experiments;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	LinearLayout screenLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		screenLayout = (LinearLayout) findViewById(R.id.layoutMain);
		Log.v("MYLOG", "En onCreate():" + Thread.currentThread().getId());
	}

	public void redOnClick(View v) {
		Log.v("MYLOG", "En redOnClick():" + Thread.currentThread().getId());
		screenLayout.setBackgroundColor(Color.RED);
	}

	public void greenOnClick(View v) {
		Log.v("MYLOG", "En greenOnClick():" + Thread.currentThread().getId());
		screenLayout.setBackgroundColor(Color.GREEN);

	}

	public void blueOnClick(View v) {
		Log.v("MYLOG", "En blueOnClick():" + Thread.currentThread().getId());
		screenLayout.setBackgroundColor(Color.BLUE);
	}

	/*
	 * public void longCalculation(View v) { Log.v("MYLOG",
	 * "En longCalculation():" + Thread.currentThread().getId()); try {
	 * Thread.currentThread().sleep(4000); } catch (InterruptedException e) { }
	 * EditText resultField = (EditText) findViewById(R.id.resultField);
	 * resultField.setText("Resultado " + resultField.getText()); }
	 */
	/*
	 * public void longCalculation(View v) { (new Thread() { public void run() {
	 * Log.v("MYLOG", "En longCalculation():" + Thread.currentThread().getId());
	 * try { Thread.currentThread().sleep(4000); } catch (InterruptedException
	 * e) { } EditText resultField = (EditText) findViewById(R.id.resultField);
	 * resultField.setText("Resultado " + resultField.getText()); } }).start();
	 * }
	 */

	public void triggerPrimecheck(View v) {
		Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
				+ ": triggerPrimecheck() starts");
		EditText inputField = (EditText) findViewById(R.id.inputField);
		long parameter = Long.parseLong(inputField.getText().toString());
		MyAsyncTask mAsyncTask = new MyAsyncTask();
		mAsyncTask.execute(parameter);
		Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
				+ ": triggerPrimecheck() ends");
	}

	private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
		
		protected Boolean doInBackground(Long... n) {
			Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
					+ ": doInBackground() starts");
			boolean isPrime = true;
			long nValue = n[0];
			if (nValue % 2 == 0)
				isPrime = false;
			else {
				long factor = 3;
				double limit = Math.sqrt(nValue) + 0.0001;
				double progressPercentage = 0;
				while (factor < limit) {
					if (nValue % factor == 0) {
						isPrime = false;
						break;
					}
					factor += 2;
					if (factor > limit * progressPercentage / 100) {
						publishProgress(progressPercentage / 100);
						progressPercentage += 5;
					}
				}
			}
			Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
					+ ": doInBackground() ends");
			return isPrime;
		}

		protected void onPreExecute() {
			Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
					+ ": onPreExecute()");
			EditText resultField = (EditText) findViewById(R.id.resultField);
			resultField.setText("");
		}

		protected void onProgressUpdate(Double... progress) {
			Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
					+ ": onProgressUpdate()");
			EditText resultField = (EditText) findViewById(R.id.resultField);
			resultField.setText((progress[0] * 100) + "% completed");
		}

		protected void onPostExecute(Boolean isPrime) {
			Log.v("MYLOG", "Thread " + Thread.currentThread().getId()
					+ ": onPostExecute()");
			EditText resultField = (EditText) findViewById(R.id.resultField);
			resultField.setText(isPrime + "");
		}
	}

}

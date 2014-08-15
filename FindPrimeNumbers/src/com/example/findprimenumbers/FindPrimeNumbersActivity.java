package com.example.findprimenumbers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FindPrimeNumbersActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void triggerFindprimes(View v) {
		EditText lowerborderField = (EditText) findViewById(R.id.lowerBorder);
		long lowerBorder = Long
				.parseLong(lowerborderField.getText().toString());
		EditText upperborderField = (EditText) findViewById(R.id.upperBorder);
		long upperBorder = Long
				.parseLong(upperborderField.getText().toString());
		MyAsyncTask mAsyncTask = new MyAsyncTask();
		mAsyncTask.execute(lowerBorder, upperBorder);
	}

	private class MyAsyncTask extends AsyncTask<Long, Long, String> {
		protected String doInBackground(Long... n) {
			for (long i = n[0]; i <= n[1]; i++) {
				boolean isPrime = true;
				if (i % 2 == 0)
					isPrime = false;
				else {
					long factor = 3;
					double limit = Math.sqrt(i) + 0.0001;
					while (factor < limit) {
						if (i % factor == 0) {
							isPrime = false;
							break;
						}
						factor += 2;
					}
				}
				if (isPrime)
					publishProgress(i);
			}
			return "END";

		}

		protected void onPreExecute() {
			EditText resultField = (EditText) findViewById(R.id.resultField);
			resultField.setText("");
		}

		protected void onProgressUpdate(Long... primeNumber) {
			EditText resultField = (EditText) findViewById(R.id.resultField);
			String output = resultField.getText() + " " + primeNumber[0];
			resultField.setText(output);
		}

		protected void onPostExecute(String message) {
			EditText resultField = (EditText) findViewById(R.id.resultField);
			String output = resultField.getText() + " " + message;
			resultField.setText(output);
		}
	}
}
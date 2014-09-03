package org.example.ejemplojquerymobile;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

	WebView navegador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		navegador = (WebView) findViewById(R.id.webkit);
		navegador.getSettings().setJavaScriptEnabled(true);
		navegador.getSettings().setBuiltInZoomControls(false);
		navegador.loadUrl("file:///android_asset/index.html");

	}
}

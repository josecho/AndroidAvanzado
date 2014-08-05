package com.example.vistaconectar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements onConectarListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		VistaConectar conectar = (VistaConectar)findViewById(R.id.vistaConectar);
		conectar.setOnConectarListener(this);
		
	}

	@Override
	public void onConectar(String ip, int puerto) {
		Toast.makeText(getApplicationContext(), "Conectando" + ip + ":" +
										puerto, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConectado(String ip, int puerto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDesconectado() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String mensaje) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
		
	}
}

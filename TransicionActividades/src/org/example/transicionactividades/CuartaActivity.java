package org.example.transicionactividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CuartaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tercera);
		overridePendingTransition(R.anim.entrada_zoom, R.anim.salida_zoom);
	}
	
	public void sepulsa(View view){
		Intent i = new Intent(this,SegundaActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.entrada_derecha, R.anim.salida_izquierda);
	}
	
	public void sepulsaTres(View view){
		Intent i = new Intent(this,CuartaActivity.class);
		startActivity(i);
		
	}
	public void sepulsaCuatro(View view){
		Intent i = new Intent(this,CuartaActivity.class);
		startActivity(i);
	}
}

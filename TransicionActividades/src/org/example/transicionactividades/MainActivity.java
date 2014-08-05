package org.example.transicionactividades;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void sepulsa(View view){
		Intent i = new Intent(this,SegundaActivity.class);
		/*startActivity(i);
		overridePendingTransition(R.anim.entrada_derecha, R.anim.salida_izquierda);*/
		ActivityOptions opts = ActivityOptions.
				makeCustomAnimation(this, R.anim.entrada_derecha, R.anim.salida_izquierda);
		startActivity(i, opts.toBundle());
	}
	
	public void sepulsaTres(View view){
		Intent i = new Intent(this,TerceraActivity.class);
		startActivity(i);
	}
	
	public void sepulsaCuatro(View view){
		Intent i = new Intent(this,CuartaActivity.class);
		startActivity(i);
	}
}

package com.example.audiolibros;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.audiolibros.fragments.DetalleFragment;
import com.example.audiolibros.fragments.SelectorFragment;
import com.example.audiolibros.fragments.SelectorFragment.OnGridViewListener;


public class MainActivity extends FragmentActivity implements OnGridViewListener {
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      SelectorAdapter.inicializarVector();
	      setContentView(R.layout.activity_main);
	      if (findViewById(R.id.fragment_container) != null
	    		  							&& savedInstanceState == null){
	    	  SelectorFragment primerFragment = new SelectorFragment();
	    	  getSupportFragmentManager().beginTransaction()
	    	  	.add(R.id.fragment_container, primerFragment).commit();
	      }
	    }

	@Override
	public void onItemSelected(int position) {
		DetalleFragment detalleFragment = (DetalleFragment) getSupportFragmentManager()
											.findFragmentById(R.id.detalle_fragment);
		//si o encontramos , xa está a vista, actualizamolo.
		if (detalleFragment != null){
			detalleFragment.updateBookView(position);
		//caso contrario, estamos na vista cun único fragment (móvil), facemos sustitución
		}else {
			DetalleFragment nuevoFragment = new DetalleFragment();
			Bundle args = new Bundle();
			args.putInt(DetalleFragment.ARG_POSITION, position);
			nuevoFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
													.beginTransaction();
			transaction.replace(R.id.fragment_container, nuevoFragment);
			//gardamos  en pila de navegación entre actividades, se pulsamos <<atrás>>, desfae transacción
			// non pecha actividade, verémolo como voltar atrás
			transaction.addToBackStack(null);
			transaction.commit();
		}
		
		//garda valor en preferencias cada vez que escollemos libro , para que funcione o método goToLastVisited()
		SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("position", position);
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		//Servizo búsqueda
		SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView)menu.findItem(R.id.menu_buscar).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		//
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.menu_preferencias:
			Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this,PreferenciasActivity.class);
			startActivity(i);
			break;
		case R.id.menu_ultimo:
			goToLastVisited();
			break;
		case R.id.menu_buscar:
			break;
		case R.id.menu_acerca:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Mensaje de Acerca De");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.create().show();
			break;
		}
		return false;
	}

	private void goToLastVisited() {
		SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
		int position = pref.getInt("position", -1);
		if (position >=0){
			onItemSelected(position);
		}else{
			Toast.makeText(this, "Sin última vista", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		if (intent.getAction().equals(Intent.ACTION_SEARCH)){
			busqueda(intent.getStringExtra(SearchManager.QUERY));
		}
	}
	
	public void busqueda(String query){
		for (int i=0; i < SelectorAdapter.bookVector.size(); i++){
			BookInfo libro = SelectorAdapter.bookVector.elementAt(i);
			if (libro.name.toLowerCase().contains(query.toLowerCase())){
				onItemSelected(i);
			}
		}
	}
	
	
}

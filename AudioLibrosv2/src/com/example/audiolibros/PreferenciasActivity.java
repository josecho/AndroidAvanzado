package com.example.audiolibros;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.audiolibros.fragments.PreferenciasFragment;

public class PreferenciasActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().
		replace(android.R.id.content, new PreferenciasFragment()).commit();
				
		/* if (findViewById(R.id.fragment_container) != null
					&& savedInstanceState == null){
			 
			FragmentTransaction t= getFragmentManager().beginTransaction()
					.replace(android.R.id.content, new PreferenciasFragment());
			 t.addToBackStack(null);
			 t.commit();
		 }else {
				PreferenciasFragment nuevoFragment = new PreferenciasFragment();
				android.app.FragmentTransaction transaction = getFragmentManager()
										.beginTransaction();
				
				transaction.replace(R.id.headlines_fragment, nuevoFragment);
				transaction.commit();
		 }*/
		
		
	}
	
	

}

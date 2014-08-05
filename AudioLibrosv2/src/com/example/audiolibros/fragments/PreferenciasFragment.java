package com.example.audiolibros.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenciasFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(com.example.audiolibros.R.xml.preferences);
	}
	
	

}

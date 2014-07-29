package com.example.audiolibros;

import com.example.audiolibros.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity {
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.main);
	      GridView gridview = (GridView)findViewById(R.id.gridview);
	      gridview.setAdapter(new SelectorAdapter(this));
	      gridview.setOnItemClickListener(new OnItemClickListener() {
	         public void onItemClick(AdapterView<?> parent, View v,
	                                           int position, long id) {
	            Toast.makeText(MainActivity.this, "Seleccionado el elemento: "
	                                   + position, Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
}

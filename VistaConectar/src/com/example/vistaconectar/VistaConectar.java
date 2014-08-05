package com.example.vistaconectar;

import com.example.vistaconectar.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VistaConectar extends LinearLayout {
	
	private onConectarListener escuchador;
	
	private EditText ip;
	private EditText puerto;
	private TextView estado;
	private Button conectar;

	public VistaConectar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.conectar, this, true);
		ip = (EditText) findViewById(R.id.ip);
		puerto = (EditText)findViewById(R.id.puerto);
		estado = (TextView)findViewById(R.id.estado);
		conectar = (Button)findViewById(R.id.conectar);
		conectar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int nPuerto;
				try {
					nPuerto = Integer.parseInt(puerto.getText().toString());
				} catch (Exception e){
					if (escuchador!=null){
						escuchador.onError("El puerto ha de ser un valor numérico");
					}
					estado.setText("ERROR");
					return;
				}
				if (nPuerto <0 || nPuerto>65535){
					if (escuchador != null){
						escuchador.onError("El puerto ha de ser un número menor de 65535");
					}
					estado.setText("ERRO");
				}else{
					if (escuchador!=null){
						escuchador.onConectar(ip.getText().toString(), nPuerto);
					}
					estado.setText("Conectando...");
				}
				//Conectar socket ...
			}
		});
	}
	
	public void setOnConectarListener(onConectarListener escuchador){
		this.escuchador=escuchador;
	}

}

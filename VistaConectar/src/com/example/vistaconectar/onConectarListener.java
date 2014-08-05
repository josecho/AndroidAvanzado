package com.example.vistaconectar;

public interface onConectarListener {
	void onConectar(String ip, int puerto);
	void onConectado(String ip, int puerto);
	void onDesconectado();
	void onError(String mensaje);

}

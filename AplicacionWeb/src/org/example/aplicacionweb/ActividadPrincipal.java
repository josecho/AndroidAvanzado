package org.example.aplicacionweb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActividadPrincipal extends Activity {

	WebView navegador;
	private ProgressBar barraProgreso;
	ProgressDialog dialogo;
	Button btnDetener, btnAnterior, btnSiguiente;
	final InterfazComunicacion miInterfazJava = new InterfazComunicacion(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		SharedPreferences prefs = getSharedPreferences("Preferencias",
				Context.MODE_PRIVATE);
		final String nombreJ1;
		nombreJ1 = prefs.getString("jugador1", null);

		navegador = (WebView) findViewById(R.id.webkit);
		// server externo
		// navegador.loadUrl("http://cursoandroid.hol.es/appweb/index.html");
		// local
		navegador.getSettings().setJavaScriptEnabled(true);
		navegador.getSettings().setBuiltInZoomControls(false);
		navegador.loadUrl("file:///android_asset/index.html");
		// abrir navegador na aplicación
		navegador.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				dialogo = new ProgressDialog(ActividadPrincipal.this);
				dialogo.setMessage("Cargando...");
				dialogo.setCancelable(true);
				dialogo.show();
				// btnDetener.setEnabled(true);
				if (comprobarConectividad()) {
					btnDetener.setEnabled(true);
				} else {
					btnDetener.setEnabled(false);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dialogo.dismiss();
				btnDetener.setEnabled(false);
				if (view.canGoBack()) {
					btnAnterior.setEnabled(true);
				} else {
					btnAnterior.setEnabled(false);
				}
				if (view.canGoForward()) {
					btnSiguiente.setEnabled(true);
				} else {
					btnSiguiente.setEnabled(false);
				}

				if (nombreJ1 != null) {
					navegador.loadUrl("javascript:cambiaNombreJugador(\"1\",\""
							+ nombreJ1 + "\");");
				}

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ActividadPrincipal.this);
				builder.setMessage(description)
						.setPositiveButton("Aceptar", null)
						.setTitle("onReceivedError");
				builder.show();
			}

		});

		barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
		navegador.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progreso) {
				barraProgreso.setProgress(0);
				barraProgreso.setVisibility(View.VISIBLE);
				ActividadPrincipal.this.setProgress(progreso * 1000);
				barraProgreso.incrementProgressBy(progreso);
				if (progreso == 100) {
					barraProgreso.setVisibility(View.GONE);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

		});

		btnDetener = (Button) findViewById(R.id.btnDetener);
		btnAnterior = (Button) findViewById(R.id.btnAnterior);
		btnSiguiente = (Button) findViewById(R.id.btnSiguiente);

		navegador.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(final String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ActividadPrincipal.this);
				builder.setTitle("Descarga");
				builder.setMessage("¿Deseas guardar el archivo?");
				builder.setCancelable(false)
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										URL urlDescarga;
										try {
											urlDescarga = new URL(url);
											new DescargarFichero()
													.execute(urlDescarga);
										} catch (MalformedURLException e) {
											e.printStackTrace();
										}
									}
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				builder.create().show();
			}
		});

		navegador.addJavascriptInterface(miInterfazJava, "jsInterfazNativa");

	}// fin onCreate

	public void detenerCarga(View v) {
		navegador.stopLoading();
	}

	public void irPaginaAnterior(View v) {
		if (comprobarConectividad()) {
			navegador.goBack();
		}
	}

	public void irPaginaSiguiente(View v) {
		if (comprobarConectividad()) {
			navegador.goForward();
		}
	}

	@Override
	public void onBackPressed() {
		if (navegador.canGoBack()) {
			navegador.goBack();
		} else {
			super.onBackPressed();
		}
	}

	private class DescargarFichero extends AsyncTask<URL, Integer, Long> {
		private String mensaje;

		@Override
		protected Long doInBackground(URL... url) {
			String urlDescarga = url[0].toString();
			mensaje = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(urlDescarga);
			InputStream inputStream = null;
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
						httpResponse.getEntity());
				inputStream = bufferedHttpEntity.getContent();
				String fileName = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/descargas";
				File directorio = new File(fileName);
				directorio.mkdirs();
				File file = new File(directorio, urlDescarga.substring(
						urlDescarga.lastIndexOf("/"), urlDescarga.indexOf("?")));
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (inputStream.available() > 0
						&& (len = inputStream.read(buffer)) != -1) {
					byteArray.write(buffer, 0, len);
				}
				fileOutputStream.write(byteArray.toByteArray());
				fileOutputStream.flush();
				mensaje = "Guardado en: " + file.getAbsolutePath();
			} catch (Exception ex) {
				mensaje = ex.getClass().getSimpleName() + " " + ex.getMessage();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
			return (long) 0;
		}

		protected void onPostExecute(Long result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ActividadPrincipal.this);
			builder.setTitle("Descarga");
			builder.setMessage(mensaje);
			builder.setCancelable(true);
			builder.create().show();
		}
	}

	private boolean comprobarConectividad() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			Toast.makeText(ActividadPrincipal.this,
					"Oops! No tienes conexión a internet", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	// Comunicacion webview-Android (aplicacións híbridas)
	public class InterfazComunicacion {
		Context mContext;

		InterfazComunicacion(Context c) {
			mContext = c;
		}

		public void mensaje(String contenido) {
			Toast.makeText(mContext, contenido, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.inicio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.inicio:
			navegador.loadUrl("javascript:mostrarInicio()");
			break;
		case R.id.jugador1:
			nombreJugador("1");
			break;
		}
		return true;
	}

	public void nombreJugador(final String jugador) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				ActividadPrincipal.this);
		alert.setTitle("Nombre jugador" + jugador);
		alert.setMessage("Nombre:");
		final EditText nombre = new EditText(getBaseContext());
		alert.setView(nombre);
		alert.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable valor = nombre.getText();
						navegador.loadUrl("javascript:cambiaNombreJugador(\""
								+ jugador + "\",\"" + valor.toString() + "\");");
						SharedPreferences prefs = getSharedPreferences(
								"Preferencias", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("jugador" + jugador, valor.toString());
						editor.commit();
					}
				});
		alert.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		alert.show();
	}

}

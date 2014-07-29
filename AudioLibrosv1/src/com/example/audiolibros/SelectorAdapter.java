package com.example.audiolibros;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectorAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	public static Vector<BookInfo> bookVector;
	
	public SelectorAdapter(Activity a){
		layoutInflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inicializarVector();
	}
	
	public int getCount(){
		return bookVector.size();
	}
	public Object getItem(int position){
		return null;
	}
	
	public long getItemId(int position){
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ImageView imageView;
		TextView audiolibroTextView;
		BookInfo bookInfo = bookVector.elementAt(position);
		View view = convertView;
		if (convertView == null){
			view = layoutInflater.inflate(R.layout.elemento_selector, null);
		}
		audiolibroTextView = (TextView) view.findViewById(R.id.titulo);
		imageView = (ImageView) view.findViewById(R.id.imageView1);
		imageView.setImageResource(bookInfo.resourceImage);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		audiolibroTextView.setText(bookInfo.name);
		return view;
	}
	public static void inicializarVector(){
		bookVector = new Vector<BookInfo>();
		bookVector.add(new BookInfo("Kappa","Akutagawa", R.drawable.kappa,
				"http://www.leemp3.com/leemp3/1/kappa_akutagawa.mp3"));
		bookVector.add(new BookInfo("Avecilla","Alas Clarin, Leopoldo", R.drawable.avecilla,
				"http://www.leemp3.com/leemp3/Avecilla_alas.mp3"));
		bookVector.add(new BookInfo("Divina Comedia", "dANTE", R.drawable.divinacomedia,
				"http://www.leemp3.com/leemp3/8/Divina%20Comedia_alighier.mp3"));
		bookVector.add(new BookInfo("Viejo Pancho, El","Alonso y Trelles, El", 
				R.drawable.viejo_pancho,
				"http://www.leemp3/leemp3/1/viejo_pancho_trelles.mp3"));
		bookVector.add(new BookInfo("Canción de Rolando","Anónimo", 
				R.drawable.cancion_rolando,
				"http://www.leemp3/leemp3/1/Cancion%20de%20Rolando_anonimo.mp3"));
		bookVector.add(new BookInfo("Matrimonio de Sabuesos","Agata Christie", 
				R.drawable.matrimonio_sabuesos,
				"http://www.dcomg.upv.es/~jtomas/android/audiolibros/01.%20Matrimonio%20De%20Sabuesos.mp3"));
		bookVector.add(new BookInfo("La iliada","Homero", 
				R.drawable.iliada,
				"http://www.dcomg.upv.es/~jtomas/android/audiolibros/la-iliada-homero184950.mp3"));
	}

}

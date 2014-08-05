package com.example.audiolibros.fragments;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.audiolibros.BookInfo;
import com.example.audiolibros.R;
import com.example.audiolibros.SelectorAdapter;

public class DetalleFragment extends Fragment implements OnTouchListener, OnPreparedListener
, MediaController.MediaPlayerControl{
	
	public static String ARG_POSITION = "position";
	Activity actividad;
	MediaPlayer mediaPlayer;
	MediaController mediaController;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		actividad = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View inflatedView = inflater.inflate(R.layout.fragment_detalle, container, false);
		Bundle args = getArguments();
		if (args != null) {
			int position = args.getInt(ARG_POSITION);
			setUpBookInfo(position, inflatedView);
		} else{
			setUpBookInfo(0,inflatedView);
		}
		
		return inflatedView;
		
	}
	
	private void setUpBookInfo(int position, View view){
		BookInfo bookInfo = SelectorAdapter.bookVector.elementAt(position);
		TextView textView = (TextView)view.findViewById(
														R.id.textView1);
		TextView audiolibroTextView = (TextView)view.findViewById(
														R.id.textView2);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		imageView.setImageResource(bookInfo.resourceImage);
		textView.setText(bookInfo.autor);
		audiolibroTextView.setText(bookInfo.name);
		
		view.setOnTouchListener(this);
		Uri video = Uri.parse(bookInfo.url);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);
		try {
			mediaPlayer.setDataSource(actividad,video);
			mediaPlayer.prepareAsync();
		} catch (IOException e){
			Log.e("Audiolibros", "ERROR:no se puede reproducir " + video,e);
		}
		mediaController = new MediaController(actividad);
	}
	
	public void updateBookView(int position){
		setUpBookInfo(position, getView());
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mediaController.show();
		return false;
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d("AudioLibros", "Entramos en onPrepared de MediaPlayer");
		//mediaPlayer.start();
		mediaController.setMediaPlayer(this);
		mediaController.setAnchorView(actividad.findViewById(R.id.main_fragment_detalle));
	}
	
	@Override public void onStop() {
		super.onStop();
		try {
			mediaPlayer.stop();
			mediaPlayer.release();
		}catch (Exception e){
			Log.d("Audiolibros", "Error en mediaPlayer.stop()");
		}
	}
	
	@Override public boolean canPause() {
		return true;
	}
	
	@Override public boolean canSeekBackward() {
		return true;
	}

	@Override public boolean canSeekForward() {
		return true;
	}
	
	@Override public int getBufferPercentage() {
		return 0;
	}
	
	@Override public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}
	
	@Override public int getDuration() {
		return mediaPlayer.getDuration();
	}
	
	@Override public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}
	
	@Override
	public void pause() {
		mediaPlayer.pause();
	}

	@Override
	public void start() {
		mediaPlayer.start();
	}

	@Override
	public void seekTo(int pos) {
		mediaPlayer.seekTo(pos);
	}

	

	

	

	

	

	

}

package org.josecho.animacionxml;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView textView = (TextView) findViewById(R.id.text_view);
		//AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animacion);
		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animacion2);
		set.setTarget(textView);
		set.start();
	}
}

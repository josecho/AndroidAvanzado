 package com.example.audiolibros.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.audiolibros.R;
import com.example.audiolibros.SelectorAdapter;

public class SelectorFragment extends Fragment implements AnimationListener , AnimatorListener{
	Activity actividad;
	GridView gridview;
	SelectorAdapter adaptador;
	OnGridViewListener mCallback;
	
	public interface OnGridViewListener {
		public void onItemSelected(int position);
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		actividad = activity;
		try{
			mCallback = (OnGridViewListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()
									+ " ha de implementar OnGridViewListener");
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.fragment_selector, container,false);
		gridview = (GridView) inflatedView.findViewById(R.id.gridview);
		adaptador = new SelectorAdapter(actividad);
		gridview.setAdapter(adaptador);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onItemSelected(position);
			}
		});
		
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
				CharSequence[] items = {"Compartir","Borrar","Insertar"};
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Animation anim1 = AnimationUtils.loadAnimation(actividad, R.anim.aumentar);
							anim1.setAnimationListener(SelectorFragment.this);
							view.startAnimation(anim1);
							
							Toast.makeText(actividad,
									"Compartiendo en redes sociales",
									Toast.LENGTH_LONG).show();
									break;
						case 1:
							//Animation anim = AnimationUtils.loadAnimation(actividad, R.anim.menguar);
							Animator anim = AnimatorInflater.loadAnimator(actividad, R.animator.menguar);
							//anim.setAnimationListener(SelectorFragment.this);
							
							anim.addListener((AnimatorListener) SelectorFragment.this);
							
							//view.startAnimation(anim);
							anim.setTarget(view);
							anim.start();
							
							SelectorAdapter.bookVector.remove(position);
							//adaptador.notifyDataSetChanged();
							break;
						case 2:
							SelectorAdapter.bookVector
									.add(SelectorAdapter.bookVector.elementAt(0));
							adaptador.notifyDataSetChanged();
							break;
						}
					}
				});
				builder.create().show();
				return true;
			}
		});
		
		return inflatedView;
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		adaptador.notifyDataSetChanged();
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		// TODO Auto-generated method stub
		adaptador.notifyDataSetChanged();
		
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}

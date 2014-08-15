package com.example.bouncingballactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BouncingBallView extends SurfaceView implements
		SurfaceHolder.Callback {

	private BouncingBallAnimationThread bbThread = null;

	private int xPosition = getWidth() / 2;
	private int yPosition = getHeight() / 2;
	private int xDirection = 20;
	private int yDirection = 40;
	private static int radius = 20;
	private static int ballColor = Color.RED;

	public BouncingBallView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		getHolder().addCallback(this);
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(ballColor);
		canvas.drawCircle(xPosition, yPosition, radius, paint);

	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (bbThread != null)
			return;
		bbThread = new BouncingBallAnimationThread(getHolder());
		bbThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		bbThread.stop = true;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return false;
		if (xDirection != 0 || yDirection != 0)
			xDirection = yDirection = 0;
		else {
			xDirection = (int) event.getX() - xPosition;
			yDirection = (int) event.getY() - yPosition;
		}
		return true;
	}

	@SuppressLint("WrongCall")
	private class BouncingBallAnimationThread extends Thread {
		public boolean stop = false;
		private SurfaceHolder surfaceHolder;

		public BouncingBallAnimationThread(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
		}

		public void run() {
			while (!stop) {
				xPosition += xDirection;
				yPosition += yDirection;
				if (xPosition < 0) {
					xDirection = -xDirection;
					xPosition = radius;
				}
				if (xPosition > getWidth() - radius) {
					xDirection = -xDirection;
					xPosition = getWidth() - radius;
				}
				if (yPosition < 0) {
					yDirection = -yDirection;
					yPosition = radius;
				}
				if (yPosition > getHeight() - radius) {
					yDirection = -yDirection;
					yPosition = getHeight() - radius - 1;
				}
				Canvas c = null;
				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						onDraw(c);
					}
				} finally {
					if (c != null)
						surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}

}

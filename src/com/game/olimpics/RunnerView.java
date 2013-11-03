package com.game.olimpics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RunnerView extends SurfaceView implements Runnable {

	SurfaceHolder mainHolder;
	Thread renderingThread = null;
	boolean isRunning = false;

	public RunnerView(Context context) {
		super(context);
		mainHolder = getHolder();
		
	}
	

	public RunnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mainHolder = getHolder();
	}
	
	
	public RunnerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mainHolder = getHolder();
	}


	/**
	 * In questo metodo mettiamo la gestione del draw dei componenti
	 */
	@Override
	public void run() {
		Paint p = new Paint();
		p.setColor(Color.BLUE);
		boolean heightSet = false;
		Canvas canvas = null;
		int height = 0;
		int width = 0;
		while(isRunning && !heightSet){
			if(!mainHolder.getSurface().isValid())
				continue;
		canvas = mainHolder.lockCanvas();
		height = canvas.getHeight();
		width = canvas.getWidth();
		heightSet = true;
		mainHolder.unlockCanvasAndPost(canvas);
		}
		Rect bg_dest = new Rect(0,0,width, height);
		RectF rect = new RectF(20,height/8,100,2*(height/8) );
		Bitmap bg_pic = BitmapFactory.decodeResource(getResources(), R.drawable.terra_ws);
		Rect bg_src = new Rect(0,0,bg_pic.getWidth(), bg_pic.getHeight());
		while(isRunning){

			
			//Se la superficie non e' valida, esce dal while
			if(!mainHolder.getSurface().isValid())
				continue;
			//Prendiamo il mutex sul canvas
			canvas = mainHolder.lockCanvas();
			canvas.drawBitmap(bg_pic, bg_src, bg_dest, null);
			canvas.drawRect(rect, p);
			mainHolder.unlockCanvasAndPost(canvas);

		}
	}
	

	/*
	 * Metodi di SurfaceView per la gestione del ciclo di vita
	 */
	public void pause(){
		isRunning = false;
		while(true){
			//Blocca il thread finche' non termina l'esecuzione degli altri processi
			try {
				renderingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		renderingThread = null;
	}

	public void resume(){
		isRunning = true;
		//Lancio il nostro thread
		renderingThread = new Thread(this);
		renderingThread.start();
	}

}

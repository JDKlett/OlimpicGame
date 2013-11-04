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
	int padding = 0;
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
		Paint [] p = new Paint[4];
		for (int i = 0; i<p.length;i++){
			p[i] = new Paint();
		}
		
		p[0].setColor(Color.YELLOW);
		p[1].setColor(Color.BLUE);
		p[2].setColor(Color.BLACK);
		p[3].setColor(Color.RED);
		
		boolean heightSet = false;
		Canvas canvas = null;
		int height = 0;
		int width = 0;
		int netWidth=0;
		int step = 0;
		while(isRunning && !heightSet){
			if(!mainHolder.getSurface().isValid())
				continue;
		canvas = mainHolder.lockCanvas();
		height = getHeight();
		width = getWidth();
		heightSet = true;
			mainHolder.unlockCanvasAndPost(canvas);
		}
		netWidth = width-getPaddingLeft()-getPaddingRight();
		step = netWidth/4;
		
		Rect bg_dest = new Rect(0,0,width-1, height-1);
		RectF [] runners = new RectF[4];
		
		for (int i = 0; i<runners.length;i++){
			runners[i]=	new RectF();
		}
		Bitmap bg_pic = BitmapFactory.decodeResource(getResources(), R.drawable.terra_ws);
		Rect bg_src = new Rect(0,0,bg_pic.getWidth(), bg_pic.getHeight());
		
		int [] targetHeights = {0,0,0,0};
		int [] speeds = {0,10,20,30};
		
		while(isRunning){
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int i =0; i<targetHeights.length;i++){
				targetHeights[i] +=speeds[i];
				if(targetHeights[i]>height+step+getPaddingLeft()){
					targetHeights[i]=0;
				}
			}
			
			//Se la superficie non e' valida, esce dal while
			if(!mainHolder.getSurface().isValid())
				continue;
			//Prendiamo il mutex sul canvas
			canvas = mainHolder.lockCanvas();
			canvas.drawBitmap(bg_pic, bg_src, bg_dest, null);
			for(int i = 0; i<runners.length;i++){
				runners[i].set(getPaddingLeft()+i*step,targetHeights[i],step*(i+1)+getPaddingLeft(),targetHeights[i]+step);
				canvas.drawRect(runners[i], p[i]);
			}
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

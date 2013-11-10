package com.game.olimpics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RunnerView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private long startTime = 0;
	private long currentTimeElapsed = 0;

	private SurfaceHolder mainHolder;
	private Thread renderingThread = null;
	private int height = 0;
	private int width = 0;
	private int netWidth = 0;
	private int step = 0;
	// questo attributo e' attualmente un paint ma deve essere esteso
	Bitmap[] runner_drawables;
	int[] heights = { 0, 0, 0, 0 };
	boolean isSurfaceReady = false;
	Canvas canvas = null;
	Rect bg_dest = null;
	RectF[] runners = null;
	RectF endingRect = null;
	Bitmap bg_pic = null;
	Bitmap bg_ending = null;

	int drawingState = DrawingStates.MAIN_DRAWING;

	public RunnerView(Context context) {
		super(context);
		init();
	}

	public RunnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RunnerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mainHolder = getHolder();
		runner_drawables = new Bitmap[4];
		runner_drawables[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.player_1);
		runner_drawables[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.player_2);
		runner_drawables[2] = BitmapFactory.decodeResource(getResources(),
				R.drawable.player_3);
		runner_drawables[3] = BitmapFactory.decodeResource(getResources(),
				R.drawable.player_4);

		mainHolder.addCallback(this);
	}

	/**
	 * In questo metodo mettiamo la gestione del draw dei componenti
	 */
	@Override
	public void run() {

		while (!isSurfaceReady)
			;

		while (drawingState == DrawingStates.MAIN_DRAWING) {

			startTime = System.currentTimeMillis();

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Se la superficie non e' valida, esce dal while

			onDrawCanvas();
			// calcolo il tempo passato rispetto ad una soglia relativa,
			// startTime calcolato una tantum
			currentTimeElapsed = (System.currentTimeMillis() - startTime);
		}

		while (drawingState == DrawingStates.END_DRAWING) {
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onDrawCanvasEnding();
		}
	}

	private synchronized void onDrawCanvas() {

		// Prendiamo il mutex sul canvas
		canvas = mainHolder.lockCanvas();
		canvas.drawBitmap(bg_pic, null, bg_dest, null);
		mainDraw();
		mainHolder.unlockCanvasAndPost(canvas);

	}

	private synchronized void onDrawCanvasEnding() {

		// Prendiamo il mutex sul canvas
		canvas = mainHolder.lockCanvas();
		canvas.drawBitmap(bg_pic, null, bg_dest, null);
		endingDraw();
		mainDraw();
		mainHolder.unlockCanvasAndPost(canvas);

	}

	private void endingDraw() {
		
		int finalLineheight = 0;

		if(heights[3]>0){
		endingRect.set(0, 0, width, height-heights[3]);
		canvas.drawBitmap(bg_ending, null, endingRect, null);
		//set dinamically to endline
		if(height-heights[3]>200){
			drawingState = DrawingStates.STOP_DRAWING;
		}
		
		}else{
			if(finalLineheight<200){
			endingRect.set(0, finalLineheight, width, (finalLineheight+=0.01));
			canvas.drawBitmap(bg_ending, null, endingRect, null);
			}else if(finalLineheight<800){
				endingRect.set(0, (finalLineheight+=0.0001), width, (finalLineheight+=0.0001));
				canvas.drawBitmap(bg_ending, null, endingRect, null);
				
			}else{
				drawingState = DrawingStates.STOP_DRAWING;
			}
		}
	}
	
	private void mainDraw() {
		for (int i = 0; i < runners.length; i++) {
			runners[i].set(getPaddingLeft() + i * step, height - heights[i]
					- step, step * (i + 1) + getPaddingLeft(), height
					- heights[i]);
			canvas.drawBitmap(runner_drawables[i], null, runners[i], null);
		}
	}

	/*
	 * Metodi di SurfaceView per la gestione del ciclo di vita
	 */
	public void pause() {
		drawingState = DrawingStates.STOP_DRAWING;
	}

	public void resume() {
		drawingState = DrawingStates.MAIN_DRAWING;
		// Lancio il nostro thread
		renderingThread = new Thread(this);
		renderingThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		canvas = mainHolder.lockCanvas();
		height = getHeight();
		width = getWidth();
		mainHolder.unlockCanvasAndPost(canvas);

		netWidth = width - getPaddingLeft() - getPaddingRight();
		step = netWidth / 4;

		bg_dest = new Rect(0, 0, width, height);
		runners = new RectF[4];
		endingRect = new RectF();
		for (int i = 0; i < runners.length; i++) {
			runners[i] = new RectF();
		}
		bg_pic = BitmapFactory.decodeResource(getResources(),
				R.drawable.terra_ws);
		bg_ending = BitmapFactory.decodeResource(getResources(), R.drawable.finish_line);
		isSurfaceReady = true;

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isSurfaceReady = false;
	}

	public void setHeights(int[] heights) {
		this.heights = heights;
	}

	public long getCurrentTimeElapsed() {
		return currentTimeElapsed;
	}

	public void setDrawingState(int drawingState) {
		this.drawingState = drawingState;
	}

	public static class DrawingStates {

		public static int MAIN_DRAWING = 0;
		public static int END_DRAWING = 1;
		public static int STOP_DRAWING = 2;

	}

}

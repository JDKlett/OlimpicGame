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
import android.view.ViewTreeObserver.OnDrawListener;

public class RunnerView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private long startTime = 0;
	private long currentTimeElapsed = 0;
	private long previousTimeElapsed = 0;

	private SurfaceHolder mainHolder;
	private Thread renderingThread = null;
	private int padding = 0;
	private int height = 0;
	private int width = 0;
	private int netWidth = 0;
	private int step = 0;
	// questo attributo e' attualmente un paint ma deve essere esteso
	Paint[] runner_drawables;
	int[] heights = { 0, 0, 0, 0 };
	boolean isRunning = false;
	boolean isSurfaceReady = false;
	Canvas canvas = null;
	Rect bg_dest = null;
	RectF[] runners = null;
	Bitmap bg_pic = null;
	Rect bg_src = null;

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
		runner_drawables = new Paint[4];
		for (int i = 0; i < runner_drawables.length; i++) {
			runner_drawables[i] = new Paint();
		}

		runner_drawables[0].setColor(Color.YELLOW);
		runner_drawables[1].setColor(Color.BLUE);
		runner_drawables[2].setColor(Color.BLACK);
		runner_drawables[3].setColor(Color.RED);

		mainHolder.addCallback(this);
	}

	/**
	 * In questo metodo mettiamo la gestione del draw dei componenti
	 */
	@Override
	public void run() {

		while (!isSurfaceReady)
			;

		
		while (isRunning) {

			startTime = System.currentTimeMillis();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Se la superficie non e' valida, esce dal while

			onDrawCanvas();

		}
	}

	private synchronized void onDrawCanvas() {

		// Prendiamo il mutex sul canvas
		canvas = mainHolder.lockCanvas();
		canvas.drawBitmap(bg_pic, bg_src, bg_dest, null);
		for (int i = 0; i < runners.length; i++) {
			runners[i].set(getPaddingLeft() + i * step, height - heights[i]
					- step, step * (i + 1) + getPaddingLeft(), height
					- heights[i]);
			canvas.drawRect(runners[i], runner_drawables[i]);
		}
		mainHolder.unlockCanvasAndPost(canvas);
		// notify();

		//calcolo il tempo passato rispetto ad una soglia relativa, startTime calcolato una tantum
		currentTimeElapsed = (System.currentTimeMillis()-startTime);
		
		
	}

	/*
	 * Metodi di SurfaceView per la gestione del ciclo di vita
	 */
	public void pause() {
		isRunning = false;
		while (true) {
			// Blocca il thread finche' non termina l'esecuzione degli altri
			// processi
			try {
				renderingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}

		renderingThread = null;
	}

	public void resume() {
		isRunning = true;
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

		for (int i = 0; i < runners.length; i++) {
			runners[i] = new RectF();
		}
		bg_pic = BitmapFactory.decodeResource(getResources(),
				R.drawable.terra_ws);
		bg_src = new Rect(0, 0, bg_pic.getWidth(), bg_pic.getHeight());

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

}

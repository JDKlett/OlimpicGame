package com.game.olimpics;

import com.game.olimpics.controller.RunnerController;

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

	private SurfaceHolder mainHolder;
	private Thread renderingThread = null;
	private RunnerController rc = null;
	private int padding = 0;
	private int height = 0;
	private int width = 0;
	private int netWidth = 0;
	private int step = 0;
	// questo attributo e' attualmente un paint ma deve essere esteso
	Paint[] runner_drawables;
	boolean isRunning = false;

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
	
		rc = new RunnerController();
	}

	/**
	 * In questo metodo mettiamo la gestione del draw dei componenti
	 */
	@Override
	public void run() {

		boolean heightSet = false;
		Canvas canvas = null;

		while (isRunning && !heightSet) {
			if (!mainHolder.getSurface().isValid())
				continue;
			canvas = mainHolder.lockCanvas();
			height = getHeight();
			width = getWidth();
			heightSet = true;
			mainHolder.unlockCanvasAndPost(canvas);
		}
		netWidth = width - getPaddingLeft() - getPaddingRight();
		step = netWidth / 4;

		Rect bg_dest = new Rect(0, 0, width - 1, height - 1);
		RectF[] runners = new RectF[4];

		for (int i = 0; i < runners.length; i++) {
			runners[i] = new RectF();
		}
		Bitmap bg_pic = BitmapFactory.decodeResource(getResources(),
				R.drawable.terra_ws);
		Rect bg_src = new Rect(0, 0, bg_pic.getWidth(), bg_pic.getHeight());

		int[] speeds = { 0, 10, 20, 30 };

		while (isRunning) {

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Se la superficie non e' valida, esce dal while
			if (!mainHolder.getSurface().isValid())
				continue;
						
			rc.updateHeights();
			// Prendiamo il mutex sul canvas
			canvas = mainHolder.lockCanvas();
			canvas.drawBitmap(bg_pic, bg_src, bg_dest, null);
			for (int i = 0; i < runners.length; i++) {
				runners[i].set(getPaddingLeft() + i * step, rc.getHeights()[i],
						step * (i + 1) + getPaddingLeft(), rc.getHeights()[i]
								+ step);
				canvas.drawRect(runners[i], runner_drawables[i]);
			}
			mainHolder.unlockCanvasAndPost(canvas);

		}
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

}

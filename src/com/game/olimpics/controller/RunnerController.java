package com.game.olimpics.controller;

import android.os.Handler;
import android.view.View;

import com.game.olimpics.R;
import com.game.olimpics.RunnerView;
import com.game.olimpics.RunnerView.DrawingStates;

import com.game.olimpics.model.RunnerModel;
import android.view.View.OnClickListener;

public class RunnerController implements Runnable, OnClickListener{

	public static final int RUNNERS_NUMBER = 4;
	Thread controllerThread = null;
	private int mState = GameStates.STATE_PAUSE;
	private RunnerModel[] runners;
	private double static_speeds [];
	private int [] heights = {0,0,0,0};
	private int game_status;
	private long game_time;
	private View [] view_list;
	private int current_click;
	
	private Handler handler;
	
	public RunnerController(View [] view_list){

		game_status = 0;
		game_time = System.currentTimeMillis();
		static_speeds = new double[4];
		runners = new RunnerModel[RUNNERS_NUMBER];

		handler = new Handler();
		handler.postDelayed(new EndingHandler(), 10000);
		for(int i=0;i<static_speeds.length;i++){
			static_speeds[i] = ((float)i+1)/100000;
		}		
		this.view_list = view_list;
		
		for(int i = 0; i<runners.length; i++){
			runners[i] = new RunnerModel();
			//static behavior
			runners[i].setSpeed(static_speeds[i]);
		}


		current_click = ClicksID.A_BUTTON;

	}

	private long speedTime = 0;
	
	private synchronized void updatePhysics(){

		long time = ((RunnerView)view_list[0]).getCurrentTimeElapsed();
		
		for(int i = 0; i<heights.length; i++){
			runners[i].updatePosition(time);
			heights[i] = (int)(runners[i].getPosition()-runners[0].getPosition());
			//System.out.println("Runner "+i+" position: "+heights[i]);
		}
		((RunnerView)view_list[0]).setHeights(heights);
		
		speedTime+=time;
		
		if(speedTime>3000){
			//runners[0].restoreSpeed();
			speedTime =0;
		}
	}

	public int [] getHeights(){

		return heights;
	}

	public void resume(){
		mState = GameStates.STATE_RUNNING;
		// Lancio il nostro thread
		controllerThread = new Thread(this);
		controllerThread.start();
	}

	public void pause(){
		mState = GameStates.STATE_PAUSE;
	}

	@Override
	public void run() {
		
		while(true){
			game_status+=5;
			updatePhysics();
		}
		
		//((RunnerView)view_list[0]).pause();
	}

	@Override
	public void onClick(View v) {
		
		if(current_click==ClicksID.A_BUTTON && R.id.b_button==v.getId()){
			if(runners[0].getSpeed()<(runners[3].getSpeed()*1.1)){ runners[0].increaseSpeed();
			System.out.println("Good!");}
			current_click=ClicksID.B_BUTTON;
		}else if(current_click==ClicksID.B_BUTTON && R.id.a_button==v.getId() ){
			//deve essere minore del massimo, per il momento il massimo e l'ultimo
			if(runners[0].getSpeed()<(runners[3].getSpeed()*1.1)){ runners[0].increaseSpeed();
			System.out.println("Good!");}
			current_click=ClicksID.A_BUTTON;
		}else{
		
		System.out.println("Wrong!");
			runners[0].decreaseSpeed();
		}
		
	}
	
	private static class ClicksID{
		
		public static int A_BUTTON = 0;
		public static int B_BUTTON = 1;
		
		
	}
	
	protected static class GameStates {
		public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
	}
	
	private class EndingHandler implements Runnable{
		public void run(){
			mState = GameStates.STATE_LOSE;
			((RunnerView)view_list[0]).setDrawingState(DrawingStates.END_DRAWING);
		}
	}
}



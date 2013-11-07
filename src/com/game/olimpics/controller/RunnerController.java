package com.game.olimpics.controller;

import java.util.HashMap;
import java.util.Vector;

import android.view.View;

import com.game.olimpics.R;
import com.game.olimpics.RunnerView;

import com.game.olimpics.model.RunnerModel;
import android.view.View.OnClickListener;

public class RunnerController implements Runnable, OnClickListener{

	public static final int RUNNERS_NUMBER = 4;
	private boolean isRunning = false;
	Thread controllerThread = null;


	private RunnerModel[] runners;
	private int static_speeds [];
	private int [] heights = {0,0,0,0};
	private int game_status;
	private View [] view_list;
	private int current_click;

	public RunnerController(View [] view_list){

		game_status = 0;

		static_speeds = new int[4];
		runners = new RunnerModel[RUNNERS_NUMBER];

		for(int i=0;i<static_speeds.length;i++){
			static_speeds[i] = (int) Math.round((Math.random()*5+1));
		}
		for(int i = 0; i<runners.length; i++){
			runners[i] = new RunnerModel();
			//static behavior
			runners[i].setSpeed(static_speeds[i]);
		}

		this.view_list = view_list;

		current_click = ClicksID.A_BUTTON;

	}

	private void updateHeights(){

		game_status+=5;

		for(int i = 0; i<heights.length; i++){
			runners[i].updatePosition();
			heights[i] = runners[i].getPosition()-runners[0].getPosition();
			//System.out.println("Runner "+i+" position: "+heights[i]);
		}

		((RunnerView)view_list[0]).setHeights(heights);

	}

	public int [] getHeights(){

		return heights;
	}

	public void resume(){
		isRunning = true;
		// Lancio il nostro thread
		controllerThread = new Thread(this);
		controllerThread.start();
	}

	public void pause(){
		isRunning = false;
		while (true) {
			// Blocca il thread finche' non termina l'esecuzione degli altri
			// processi
			try {
				controllerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}

		controllerThread = null;
	}

	@Override
	public void run() {

		while(isRunning){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateHeights();
		}
	}

	@Override
	public void onClick(View v) {
		
		
		if(current_click==ClicksID.A_BUTTON && R.id.b_button==v.getId()){
			runners[0].increaseSpeed();
			System.out.println("Good!");
			current_click=ClicksID.B_BUTTON;
			
		}else if(current_click==ClicksID.B_BUTTON && R.id.a_button==v.getId() ){
			runners[0].increaseSpeed();
			System.out.println("Good!");
			current_click=ClicksID.A_BUTTON;
			
		}else{
		
		System.out.println("Wrong!");
		}
		
	}
	
	private static class ClicksID{
		
		public static int A_BUTTON = 0;
		public static int B_BUTTON = 1;
		
		
	}
}



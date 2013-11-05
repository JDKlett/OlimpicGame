package com.game.olimpics.controller;

import java.util.Vector;

import com.game.olimpics.model.RunnerModel;

public class RunnerController{

	public static final int RUNNERS_NUMBER = 4;
	
	private RunnerModel[] runners;
	private int [] static_speeds = {5,10,7,9};
	private int [] heights = {0,0,0,0};
		
	public RunnerController(){
		
		runners = new RunnerModel[RUNNERS_NUMBER];
		
		for(int i = 0; i<runners.length; i++){
			runners[i] = new RunnerModel();
			//static behavior
			runners[i].setSpeed(static_speeds[i]);
		}
		
	}
	
	public void updateHeights(){
		
		for(int i = 0; i<heights.length; i++){
			heights[i] +=static_speeds[i];
		}
		
	}
	
	public int [] getHeights(){
		
		return heights;
	}

}

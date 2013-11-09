package com.game.olimpics.model;

public class RunnerModel {

	private double speed;
	private double position;
	private boolean bonus=false;

	public RunnerModel() {
		speed = 0;
		position = 0;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}


	public double getPosition() {
		return position;
	}

	public synchronized void updatePosition(long timeElapsed) {
		position+=(speed*timeElapsed);
	}
	
	public synchronized void increaseSpeed(){
		speed = speed*1.05;
	}
	
	public synchronized void decreaseSpeed(){
		speed = speed/1.5;
	}
	
}

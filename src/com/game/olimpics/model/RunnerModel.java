package com.game.olimpics.model;

public class RunnerModel {

	private int speed;
	private int position;

	public RunnerModel() {
		speed = 0;
		position = 0;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public int getPosition() {
		return position;
	}

	public void updatePosition(long timeElapsed) {
		position+=(speed*timeElapsed/1000);
	}
	
	public void increaseSpeed(){
		position+=3*(speed);
	}
}

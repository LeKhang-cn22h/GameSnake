package model;

import controller.SharedData;

public class Score {
	private int currentScore;

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public Score(int currentScore) {
		super();
		this.currentScore = currentScore;
	}
	public void resetScore() {
		this.currentScore=0;
	}
	public void increaseScore() {
	    double speed = SharedData.getSpeed(); // Lấy giá trị Speed từ SharedData
	    if (speed == 100) {
	        this.currentScore += 20;
	    } else if (speed == 200) {
	        this.currentScore += 10;
	    } else if (speed == 300) {
	        this.currentScore += 5;
	    }
	}

}

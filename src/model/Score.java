package model;

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
	public void increaseScore(int points) {
		this.currentScore+=points;
	}
}

package game;

public class Score {
	private double score;
	private int multiplier;
	
	public static final double INITIAL_SCORE = 0;
	public static final double HIT_ASTEROID_SCORE = 10;
	public static final int INITIAL_MULTIPLIER = 1;
	public static final int HIT_ASTEROID_ADD_MULTIPLIER = 1;
	
	public Score() {
		this.score = INITIAL_SCORE;
		this.multiplier = INITIAL_MULTIPLIER;
	}
	
	public double getScore() {
		return score;
	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
	public void update(double dt) {
		
	}
	
	public void notifyAsteroidHit() {
		addPoints(HIT_ASTEROID_SCORE);
		addMultiplier(HIT_ASTEROID_ADD_MULTIPLIER);
	}
	
	private void addPoints(double points) {
		score += points * multiplier;
	}
	
	private void addMultiplier(int add) {
		multiplier += add;
		if(multiplier <= 0) multiplier = INITIAL_MULTIPLIER;
	}
}

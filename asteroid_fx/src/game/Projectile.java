package game;

import tools.Vector;

public class Projectile {
	private Vector position, velocity;
	private int lifeDuration;
	
	private boolean isAlive;
	
	private static final int INITIAL_LIFE_DURATION = 10;
	
	public Projectile(Vector position, Vector velocity) {
		this.position = position;
		this.velocity = velocity;
		lifeDuration = INITIAL_LIFE_DURATION;
		isAlive = true;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void update(double dt) {
		
	}
}
